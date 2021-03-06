(ns report.components.app-content
  (:require [reagent.core :as r]
            [report.test-results.statuses :refer [get-reputation sort-statuses-by-weight sort-statuses-by-vis-order bad-status? good-status? neutral-status? get-worse get-best]]
            [report.test-results.structure :as s :refer [get-assets leaf-content is-that-run? is-node? is-scenario? is-run? get-runs-count]]
            [report.test-results.extra-params :refer [build-name]]
            [report.components.badges :refer [badged-text]]
            [report.components.runs-meta :refer [meta-data-render]]
            [report.components.fails-list :refer [fails-list]]
            [report.components.errors-list :refer [errors-list]]
            [report.components.assets-list :refer [assets-list]]
            [report.components.truncated-string :refer [truncated-string]]
            [report.components.tooltip :as tooltip]
            [report.components.block-link :as bl]
            [report.components.dropdown :refer [dropdown]]
            [report.components.styles.content-pane :as cp]
            [report.components.common.utils :as u]
            [report.utils.string :refer [keyword->str]]
            [report.utils.log :refer [log log-o]]
            [report.utils.net :refer [set-href!]]
            [report.routing :as routing :refer [path->uri]]
            [report.components.status-filter :refer [status-filter hovered? w-a-active? active? any-active?]]
            [report.test-results.path :refer [safe-path desafe-path flatten-path path->str]]
            [report.components.buttons :refer [state-button button]]
            [report.components.styles.items-list :as il]
            [report.components.styles.links :as l]
            [report.components.styles.core :as sc]
            [clojure.string :as string]))




(def default-runs-limit 50)
(def default-runs-more-count 50)

(def ^:private status-descs {"SUCCESS"   "Everything went ok."
                             "ERROR"     "Exception/s occured during test scenario execution. Probably the test is broken."
                             "FAIL"      "One or more asserts failed."
                             "UNDEFINED" "Such status occured when a test didn't provide any status. Looks like neither success nor fail."
                             "SKIPPED"   "Such status occured when a test was not applicable to certain data source."})


(defn- mk-tooltip-map [status align]
  (let [quarantine (boolean (re-find #"_Q" status))
        status-cleaned (string/replace status #"_Q" "")
        status-desc (get status-descs status-cleaned)
        final-status-desc (if quarantine
                            [:span [:p "QUARANTINED"] [:p status-desc]]
                            status-desc)]
    {:on-mouse-enter (partial tooltip/show-tooltip final-status-desc align)
     :on-mouse-leave tooltip/hide-tooltip}))

(defn- trigger-refresh-scroll [this]
  (.trigger (js/$ (r/dom-node this)) "refresh-scroll"))


(defn common-prefix [paths]
  (if (some? paths)
    (let [parts-per-path (map #(drop-last (string/split (keyword->str %) #"\.")) paths)
          ;_ (log-o "parts-per-path" parts-per-path)
          parts-per-position (apply map vector parts-per-path)
          ;_ (log-o "parts-per-position" parts-per-position)
          prefix (string/join "."
                              (for [parts parts-per-position :while (apply = parts)]
                                (first parts)))
          ;_ (log-o "prefix" prefix)
          ]
      (if-not (empty? prefix) (str prefix ".")))
    ""))

(defn- mk-badges-list [items reputation ids-a]
  (for [item items
        :let [id (swap! ids-a inc)]]
    ^{:key id} [badged-text reputation item true]))

(defn- prepare-badge-str-f [prefix-l [k v]]
  (let [
        ;_ (log-o "k " k)
        ;_ (log-o "v " v)
        ;_ (log-o "prefx-l " prefix-l)
        postfix (if (> v 1)
                  (str " x" v)
                  "")
        ;_ (log-o "postfix " postfix)
        ]
    (str (subs (keyword->str k) prefix-l) postfix)))


(defn- list-row [{:keys [text statuses summary parent-statuses status-filter-a href accent]}]
  (let [
        accent-class (when accent 'il/neu-list-row--accent)
        ;_ (log "4")
        status-filter (when status-filter-a @status-filter-a)
        ;_ (log "5")
        vis (any-active? (keys statuses) status-filter)
        ;_ (log "6")
        ;_ (when (some? summary) (log-o "summary " summary))
        summary-count (if-not summary
                        0
                        (reduce + 0 (into [] (filter some? (flatten (map vals (vals summary)))))))
        ;_ (log-o "summary count " summary-count)
        ]
    (when vis [:div (u/attr {:classes (list 'il/neu-list-row
                                            'il/neu-list-row--no-padding
                                            accent-class)})
               [bl/block-link :href href :sub-items
                (into
                  [[:div (u/attr {:classes '(il/neu-list-column il/neu-list-column--grow il/neu-list-column--left il/neu-list-column--left-padded)})
                    [truncated-string text]]
                   (if (pos? summary-count)
                     (let [
                           ;_ (log "1")
                           ;_ (log-o "summary " summary)
                           errors-common-prefix-l (count (common-prefix (keys (get summary :errors))))

                           ;_ (log "2")
                           failss-common-prefix-l (count (common-prefix (keys (get summary :fails))))
                           ;_ (log "3")
                           ids (atom 0)]
                       [:div (u/at :classes '(il/neu-list-column
                                               il/neu-list-column--more-grow
                                               il/neu-list-column--padded
                                               il/neu-list-column--right
                                               il/neu-list-column--overflow-hidden))
                        [:div (u/at :classes (list 'sc/text-align-right
                                                   'sc/quarter-unit-padding))
                         (mk-badges-list (map (partial prepare-badge-str-f 0) (get summary :badges)) :neutral ids)
                         (mk-badges-list (map (partial prepare-badge-str-f errors-common-prefix-l)
                                              (get summary :errors)) :accent ids)
                         (mk-badges-list (map (partial prepare-badge-str-f failss-common-prefix-l)
                                              (get summary :fails)) :bad ids)]]))]
                  (for [[idx status] (map-indexed vector parent-statuses)
                        :let [status-count (get statuses status nil)
                              active (w-a-active? status status-filter)
                              hover (hovered? status status-filter)
                              active-status (when-not active 'il/neu-list-column--shadowed)
                              hover-status (when hover 'il/neu-list-column--hovered)]]
                    (if status-count
                      ^{:key idx} [:div (u/attr {:classes (list 'il/neu-list-column
                                                                active-status
                                                                hover-status)})
                                   [badged-text (get-reputation status) status-count]]
                      ^{:key idx} [:div (u/attr {:classes (list 'il/neu-list-column
                                                                active-status
                                                                hover-status)})])))]])))



(defn- sub-struct-list [{:keys [status-map sub-items parent-statuses summary-map parent-path status-filter-a get-href-fn]}]
  ;(log "sub-struct-list")
  ;(log-o "sub-items " sub-items)
  [:div
   (for [[idx item] (map-indexed vector sub-items)
         :let [
               ;_ (log-o "item " item)
               item-path (conj parent-path item)
               flat-item-path (flatten-path item-path)
               item-statuses (get status-map flat-item-path)
               ;_ (log-o "item-path " flat-item-path)
               ;_ (log-o "scen-info " (get summary-map flat-item-path))
               ]]
     ^{:key idx} [list-row {:text            (path->str item)
                            :statuses        item-statuses
                            :summary         (get summary-map flat-item-path)
                            :parent-statuses parent-statuses
                            :status-filter-a status-filter-a
                            :href            (get-href-fn item)
                            :accent          false}])])

(defn- gen-uri-jumps [{:keys [test-data-map struct runs parent-path]}]
  ;(log "gen-uri-jumps called")
  ;(log-o "test-data-map " test-data-map)
  (let [get-single-run-target (fn [runs scen-id]
                                (let [scen-runs (get runs scen-id)
                                      ;_ (log-o "scen-runs " scen-runs)
                                      targets (js->clj (.keys js/Object scen-runs))
                                      ;_ (log-o "targets " targets)
                                      ]
                                  (if (= 1 (count targets))
                                    (first targets)
                                    nil)))
        f (fn [coll x]
            (let [full-path (conj parent-path x)
                  full-path-adjusted (if-not (is-scenario? struct full-path)
                                       full-path
                                       (let [
                                             ;_ (log-o "full-path " full-path)
                                             test-data-path (rest (flatten-path full-path))
                                             ;_ (log-o "test-data-path " test-data-path)
                                             scen-info (get test-data-map test-data-path)
                                             ;_ (log-o "scen-info " scen-info)
                                             scen-id (get scen-info :id)
                                             ;_ (log-o "runs " runs)
                                             single-target (get-single-run-target runs scen-id)
                                             ;_ (log-o "single-target " single-target)
                                             ]
                                         (if single-target
                                           (conj full-path (safe-path single-target))
                                           full-path)))
                  ;_ (log-o "full-path-adjusted " full-path-adjusted)
                  ]
              (assoc coll x (path->uri full-path-adjusted))))
        sub-items (keys (get-in struct parent-path))
        ;_ (log-o "struct " struct)
        ;_ (log-o "parent-path " parent-path)
        ;_ (log-o "sub-items " sub-items)
        jump-per-item-map (reduce f {} sub-items)
        ;_ (log-o "jump-per-item-map " jump-per-item-map)
        ]
    (fn [item]
      (get jump-per-item-map item))))

(defn- category [{:keys [cat-name runs struct test-data-map status-map parent-statuses status-filter-a]}]
  (let [cat-path [cat-name]
        cat-statuses (get status-map cat-path)
        ;_ (log-o "cat-statuses " cat-statuses)
        sub-items (keys (get-in struct cat-path))
        ;_ (log-o "sub-items " sub-items)
        ]
    (fn []
      (let [vis (if (nil? status-filter-a)
                  true
                  (any-active? (keys cat-statuses) @status-filter-a))]
        (when vis [:div
                   [list-row {:text            cat-name
                              :statuses        cat-statuses
                              :parent-statuses parent-statuses
                              :status-filter-a status-filter-a
                              :href            (path->uri cat-path)
                              :accent          true}]
                   [sub-struct-list {:status-map      status-map
                                     :sub-items       sub-items
                                     :parent-statuses parent-statuses
                                     :parent-path     cat-path
                                     :status-filter-a status-filter-a
                                     :get-href-fn     (gen-uri-jumps {:test-data-map test-data-map
                                                                      :struct        struct
                                                                      :runs          runs
                                                                      :parent-path   cat-path})}]
                   [:div.list-row.list-row--border-less]])))))


(defn home-view [{:keys [struct runs test-data-map status-map status-filter-a]}]
  (let [root-status-map (get status-map [])
        statuses (sort-statuses-by-vis-order > (keys root-status-map))
        categories (sort (keys struct))
        ;_ (log-o "cats " categories)
        ]
    (r/create-class
      {:component-did-update trigger-refresh-scroll
       :component-did-mount  trigger-refresh-scroll
       :component-function   (fn []
                               [:div
                                [:div.list-row.list-row--height-xl.list-row--border-less.list-row--m-bottom-m.list-row--no-padding
                                 [:div.list-column.list-column--grow.list-column--left
                                  [:h1.margin-less "Overview"]]
                                 (status-filter statuses root-status-map status-filter-a)]

                                [list-row {:text            "Total:"
                                           :statuses        root-status-map
                                           :parent-statuses statuses}]
                                [:div.list-row.list-row--border-less]
                                (for [cat-indexed (map-indexed vector categories)
                                      :let [[idx cat] cat-indexed]]
                                  ^{:key idx} [category {:cat-name        cat
                                                         :struct          struct
                                                         :runs            runs
                                                         :test-data-map   test-data-map
                                                         :status-map      status-map
                                                         :parent-statuses statuses
                                                         :status-filter-a status-filter-a}])
                                ])})))



(defn is-flat-list? [{:keys [status-map parent-path items]}]
  (let [f (fn [parent-path status-map res x]
            (let [path (flatten-path (conj parent-path x))
                  item-status-map (get status-map path)
                  statuses-count (count item-status-map)
                  status-count (first (vals item-status-map))
                  new-res (and (= status-count 1) (= statuses-count 1))]
              (and res new-res)))]
    (reduce (partial f parent-path status-map) true items)))



(defn flat-list [{:keys [status-map parent-path summary-map items status-filter-a get-href-fn]}]
  (let [status-filter @status-filter-a]
    [:div
     [:div (u/attr {:classes (list 'il/neu-list-row
                                   'il/neu-list-row--accent)})
      ;.list-row.list-row--accent
      [:div (u/attr {:classes (list 'il/neu-list-column
                                    'il/neu-list-column--grow
                                    'il/neu-list-column--left)})
       ;.list-column.list-column--grow.list-column--left
       "Path:"]
      [:div (u/attr {:classes (list 'il/neu-list-column
                                    'il/neu-list-column--width-l)}) "Status"]]
     (for [[idx item] (map-indexed vector items)
           :let [full-path (flatten-path (conj parent-path item))
                 item-status-map (get status-map full-path)
                 summary (get summary-map full-path)
                 summary-count (if-not summary
                                 0
                                 (reduce + 0 (into [] (filter some? (flatten (map vals (vals summary)))))))
                 status (name (first (keys item-status-map)))
                 ;status-class (cond
                 ;               (good-status? status) "list-row--success"
                 ;               (bad-status? status) "list-row--error"
                 ;               :else "")
                 vis (w-a-active? status status-filter)
                 ;_ (log-o "item: " item)
                 str-item (path->str item)
                 ]]
       (when vis
         ^{:key idx} [:div (u/attr {:classes (list 'il/neu-list-row
                                                   'il/neu-list-row--no-padding)})
                      ;.list-row.list-row--hoverable
                      [bl/block-link :href (get-href-fn item)
                       :sub-items [[:div (u/attr {:classes (list 'il/neu-list-column
                                                                 'il/neu-list-column--grow
                                                                 'il/neu-list-column--left
                                                                 'il/neu-list-column--padded)})
                                    [truncated-string str-item]]
                                   (if (pos? summary-count)
                                     (let [
                                           ;_ (log "1")
                                           errors-common-prefix-l (count (common-prefix (keys (get summary :errors))))
                                           ;_ (log "2")
                                           failss-common-prefix-l (count (common-prefix (keys (get summary :fails))))
                                           ;_ (log "3")
                                           ids (atom 0)
                                           ]
                                       [:div (u/at :classes '(il/neu-list-column
                                                               il/neu-list-column--more-grow
                                                               il/neu-list-column--padded
                                                               il/neu-list-column--right
                                                               il/neu-list-column--overflow-hidden))
                                        [:div (u/at :classes (list 'sc/text-align-right
                                                                   'sc/quarter-unit-padding))
                                         (mk-badges-list (map (partial prepare-badge-str-f 0) (get summary :badges)) :neutral ids)
                                         (mk-badges-list (map (partial prepare-badge-str-f errors-common-prefix-l)
                                                              (get summary :errors)) :accent ids)
                                         (mk-badges-list (map (partial prepare-badge-str-f failss-common-prefix-l)
                                                              (get summary :fails)) :bad ids)]]))
                                   [:div (u/attr {:classes (list 'il/neu-list-column
                                                                 'il/neu-list-column--width-l)})
                                    [badged-text (get-reputation status) status]]]]]))]))

(defn node-view [{:keys [struct runs summary-map test-data-map status-map status-filter-a nav-position-a]}]
  (let [title (r/atom nil)]
    (r/create-class
      {:component-did-update trigger-refresh-scroll
       :component-did-mount  trigger-refresh-scroll
       :component-function   (fn []
                               (let [path (routing/get-path @nav-position-a)
                                     node-title (path->str (peek path))
                                     node-status-map (get status-map (flatten-path path))
                                     statuses (sort-statuses-by-vis-order > (keys node-status-map))
                                     ;_ (log "node-view rendered")
                                     ;_ (log-o "statuses " statuses)
                                     ;_ (log-o "node-map " node-status-map)
                                     sub-items (keys (get-in struct path))
                                     ;_ (log-o "sub-items " sub-items)
                                     flat-list? (is-flat-list? {:status-map  status-map
                                                                :parent-path path
                                                                :items       sub-items})
                                     ;_ (log-o "flat list? " flat-list?)

                                     ]
                                 (reset! title node-title)
                                 [:div
                                  [:div.list-row.list-row--height-xl.list-row--border-less.list-row--m-bottom-m.list-row--no-padding
                                   [:div.list-column.list-column--grow.list-column--left
                                    [:h1.margin-less [truncated-string title]]]
                                   (status-filter statuses node-status-map status-filter-a)]

                                  (if-not flat-list?
                                    (list
                                      ^{:key 1} [:div.list-row.list-row--accent
                                                 [:div.list-column.list-column--grow.list-column--left "Path:"]]

                                      ^{:key 2} [sub-struct-list {:status-map      status-map
                                                                  :summary-map     summary-map
                                                                  :sub-items       sub-items
                                                                  :parent-statuses statuses
                                                                  :parent-path     path
                                                                  :status-filter-a status-filter-a
                                                                  :get-href-fn     (gen-uri-jumps {:test-data-map test-data-map
                                                                                                   :struct        struct
                                                                                                   :runs          runs
                                                                                                   :parent-path   path})}])
                                    [flat-list {:status-map      status-map
                                                :summary-map     summary-map
                                                :parent-path     path
                                                :items           sub-items
                                                :status-filter-a status-filter-a
                                                :get-href-fn     (gen-uri-jumps {:test-data-map test-data-map
                                                                                 :runs          runs
                                                                                 :struct        struct
                                                                                 :parent-path   path})}])]))})))



#_(defn- extract-target-status-old [get-runs-fn limit active?]
  (let [
        runs (get-runs-fn)
        _ (log-o "runs " runs)
        f (fn [coll x]
            (let [
                  _ (log-o "x" x)
                  {:keys [target run-info]} x
                  _ (log-o "target" target)
                  _ (log-o "run-info" run-info)
                  status (get run-info :status)
                  old-list (get coll :list)
                  old-limit-remains (get coll :limit-remains)]
              (if (and (active? status) (pos? old-limit-remains))
                (-> coll
                    (assoc :list (conj old-list [target status]))
                    (assoc :limit-remains (dec old-limit-remains)))
                coll)))]
    (-> (reduce f {:list [] :limit-remains limit} runs)
        (get :list))))

(defn- extract-target-status [get-runs-fn scen-quarantine limit active?]
  (let [
        runs (get-runs-fn)
        ;_ (log-o "runs " runs)
        run-targets (lazy-seq (.keys js/Object runs))
        ;_ (log (first run-targets))
        ]
    (loop [limit-remains limit
           rest-targets run-targets
           acc []
           ]
      (if (or (zero? limit-remains) (empty? rest-targets))
        acc
        (let [target (first (take 1 rest-targets))
              run-info (get runs target)
              new-rest-targets (drop 1 rest-targets)
              status (get run-info :status)
              new-status (if (contains? scen-quarantine target)
                           (str status "_Q")
                           status)
              [new-acc new-limit] (if-not (active? new-status)
                                    [acc limit-remains]
                                    [(conj acc [target new-status]) (dec limit-remains)])]
          (recur new-limit new-rest-targets new-acc))))))


(defn- extract-target-summary [{:keys [fails errors badges]}]
  (let [get-type (fn [x] (get x :type))
        only-types-fails (map get-type fails)
        only-types-errors (map get-type errors)]
    {:errors only-types-errors
     :fails  only-types-fails
     :badges badges}))



(defn- extract-target [get-runs-fn scen-quarantine limit active?]
  (let [runs (get-runs-fn)
        run-targets (.keys js/Object runs)]
    (into [] (take limit
                   (sequence
                     (comp (map (fn [target]
                                  (let [run-info (get runs target)
                                        status (get run-info :status)
                                        new-status (if (contains? scen-quarantine target)
                                                     (str status "_Q") status)]
                                    (if (active? new-status)
                                      [target new-status (extract-target-summary run-info)]
                                      nil))))
                           (filter some?))
                     run-targets)))))



(defn- count-f [m x]
  (->> (get m x 0)
       inc
       (assoc m x)))


(defn- scenario-runs [{:keys [get-runs-fn scen-quarantine status-filter-a runs-limit path]}]
  (r/create-class
    {:component-did-update trigger-refresh-scroll
     :component-function   (fn []
                             ;(log "scenario runs")
                             (let [target-data (extract-target get-runs-fn scen-quarantine @runs-limit #(active? % status-filter-a))
                                   ;_ (log-o "target-data: " target-data)
                                   ]
                               [:div
                                [:div (u/at :classes '(il/neu-list-row il/neu-list-row--accent))
                                 [:div (u/at :classes '(il/neu-list-column il/neu-list-column--grow il/neu-list-column--left)) "Target"]
                                 [:div (u/at :classes '(il/neu-list-column
                                                         il/neu-list-column--more-grow
                                                         il/neu-list-column--padded
                                                         il/neu-list-column--right)) "Badges"]
                                 [:div (u/at :classes '(il/neu-list-column il/neu-list-column--width-l)) "Status"]]
                                (for [[idx target-item] (map-indexed vector target-data)
                                      :let [[target status summary] target-item
                                            {:keys [errors fails badges]} summary
                                            errors-counted (reduce count-f {} errors)
                                            errors-common-prefix-length (count (common-prefix (keys errors-counted)))
                                            errors-prepared (map (partial prepare-badge-str-f errors-common-prefix-length)
                                                                 errors-counted)
                                            ;_ (log-o "errors-common-prefix-length " errors-common-prefix-length)
                                            fails-counted (reduce count-f {} fails)
                                            ;_ (log-o "fails-counted " fails-counted)
                                            fails-common-prefix-length (count (common-prefix (keys fails-counted)))
                                            ;_ (log-o "fails-common-prefix-length " fails-common-prefix-length)
                                            fails-prepared (map (partial prepare-badge-str-f fails-common-prefix-length)
                                                                fails-counted)
                                            ;_ (log-o "fails-prepared " fails-prepared)
                                            total-count (apply + [(count errors-prepared) (count fails-prepared) (count badges)])
                                            ids (atom 0)
                                            ]]
                                  ^{:key idx} [:div (u/at :classes '(il/neu-list-row
                                                                      il/neu-list-row--no-padding))
                                               [bl/block-link :href (path->uri (conj path target))
                                                :sub-items (list
                                                             [:div (u/attr {:classes '(il/neu-list-column
                                                                                        il/neu-list-column--grow
                                                                                        il/neu-list-column--left
                                                                                        il/neu-list-column--padded)})
                                                              [:div (u/at :classes 'sc/width-100)
                                                               [truncated-string target]]]
                                                             (if (pos? total-count)
                                                               [:div (u/at :classes '(il/neu-list-column
                                                                                       il/neu-list-column--more-grow
                                                                                       il/neu-list-column--padded
                                                                                       il/neu-list-column--right))
                                                                [:div (u/at :classes (list 'sc/text-align-right
                                                                                           'sc/quarter-unit-padding))
                                                                 (mk-badges-list badges :neutral ids)
                                                                 (mk-badges-list errors-prepared :accent ids)
                                                                 (mk-badges-list fails-prepared :bad ids)]])
                                                             [:div (u/attr {:classes '(il/neu-list-column
                                                                                        il/neu-list-column--width-l)})
                                                              [badged-text (get-reputation status) status]])]])]))}))


(defn doc [doc-strings]
  (when-not (empty? doc-strings)
    [:div.vertical-block
     (for [[idx str] (map-indexed vector doc-strings)]
       ^{:key idx} [:p str])]))

(defn- scenario-view [{:keys [scenario-info get-runs-fn scen-quarantine scenario-name scenario-status-map status-filter-a path]}]
  (let [runs-limit (r/atom default-runs-limit)
        extend-limit #(swap! runs-limit (partial + default-runs-more-count))
        unlim #(reset! runs-limit (get-runs-count (get-runs-fn)))]
    (fn []
      (let [statuses (sort-statuses-by-vis-order > (keys scenario-status-map))
            doc-strings (string/split (get scenario-info :doc) #"\n\n")
            overview (get scenario-info :overview)
            ;_ (log-o "scenario-info: " scenario-info)
            ;_ (log-o "doc strings: " doc-strings)
            ]
        [:div

         [:div.list-row.list-row--height-xl.list-row--border-less.list-row--m-bottom-m.list-row--no-padding
          [:div.list-column.list-column--grow.list-column--left
           [:h1.margin-less [truncated-string scenario-name]]]
          (status-filter statuses scenario-status-map status-filter-a)]

         [doc doc-strings]
         [meta-data-render overview :header "Overview"]
         [scenario-runs {:get-runs-fn     get-runs-fn
                         :scen-quarantine scen-quarantine
                         :status-filter-a status-filter-a
                         :runs-limit      runs-limit
                         :path            path}]
         [:div.vertical-block
          [:div.list-row.list-row--border-less.list-row--no-padding.list-row--height-s
           [:div.list-column.list-column--grow]
           [:div.list-row__group
            [:div..list-column.list-column--clickable.list-column--stretch.list-column--separator {:on-click extend-limit} "More"]
            [:div..list-column.list-column--clickable.list-column--stretch.list-column--separator {:on-click unlim} "All"]]]]]))))



(defn run-view [target scen-quarantine js-run doc-strings]
  (fn []
    (let [run (js->clj js-run :keywordize-keys true)
          ;_ (log-o "run " run)
          meta-data (get run :meta)
          ;_ (log-o "meta-data " meta-data)
          status (get run :status)
          ;_ (log-o "status " status)
          new-status (if (contains? scen-quarantine target)
                       (str status "_Q")
                       status)
          ]
      [:div

       #_[:div [dropdown {:coll      ["1" "2" "3" "4" "5"]
                        :width     :s
                        :current   :1
                        :any?      false
                        :select-fn #(log-o "select-fn params" %)}]]
       [:div.list-row.list-row--height-l.list-row--border-less.list-row--m-bottom-m.list-row--no-padding
        [:div.list-column..list-column--auto-width [:h1.margin-less (mk-tooltip-map new-status :left) (str new-status ":\u00A0\u00A0")]]
        [:div.list-column.list-column--grow.list-column--left [:h1.margin-less [truncated-string target]]]]

       [doc doc-strings]
       [meta-data-render meta-data]
       [fails-list (get run :fails)]
       [errors-list (get run :errors)]
       [assets-list (get-assets run)]
       ])))




(defn fails-node [{:keys [struct data-map]} path mk-ref-f]
  (let [sub-struct (if path
                     (get-in struct path)
                     struct)
        ;_ (log-o "is-vector" (vector? path))
        ;_ (log-o "path type" (type path))
        ;_ (log-o "sub-struct" sub-struct)
        ]
    [:div
     [:div (u/at :classes '(il/neu-list-row
                             il/neu-list-row--accent))
      [:div (u/at :classes '(il/neu-list-column
                              il/neu-list-column--grow
                              il/neu-list-column--left)) "Fail Type:"]
      [:div (u/at :classes '(il/neu-list-column
                              il/neu-list-column--width-l)) "Count:"]]
     (for [[idx item] (map-indexed vector (keys sub-struct))
           :let [
                 ;_ (log-o "item " item)
                 ;_ (log-o "path " path)
                 full-path (flatten (conj path item))
                 ;_ (log-o "full-path" full-path)
                 c (get-in data-map [full-path :count] 0)]]
       ^{:key idx} [:div (u/at :classes (list 'il/neu-list-row
                                              'il/neu-list-row--no-padding))
                    [bl/block-link :href (mk-ref-f (conj path item))
                     :sub-items [[:div (u/at :classes (list 'il/neu-list-column
                                                            'il/neu-list-column--grow
                                                            'il/neu-list-column--left
                                                            'il/neu-list-column--left-padded)) [truncated-string (path->str item)]]
                                 [:div (u/at :classes (list 'il/neu-list-column
                                                            'il/neu-list-column--width-l)) [badged-text :bad c]]]]])]))

(defn fails-leaf [{:keys [fail-mapping ids id->path runs fail-name]}]
  (let [fails-per-id (map vector (map (partial get fail-mapping) ids) ids)
        targets (reduce (fn [coll [m id]]
                          (into coll (map vector (get m fail-name) (repeat id)))) [] fails-per-id)

        runs-limit (r/atom default-runs-limit)
        extend-limit #(swap! runs-limit (partial + default-runs-more-count))
        unlim #(reset! runs-limit (count targets))
        ;_ (log-o "targets " targets)

        ]
    [:div
     [:div (u/at :classes '(il/neu-list-row
                             il/neu-list-row--accent))
      [:div (u/at :classes '(il/neu-list-column
                              il/neu-list-column--grow
                              il/neu-list-column--left)) "Target:"]
      [:div (u/at :classes '(il/neu-list-column
                              il/neu-list-column--width-l)) "Count:"]]

     (for [[idx target] (map-indexed vector targets)
           :let [[target id] target
                 fails (get-in runs [id target :fails])
                 ;_ (log-o "fails " fails)
                 filtered-fails (filter #(= fail-name (get % :type)) fails)
                 ;_ (log-o "filtered fails " filtered-fails)
                 c (count filtered-fails)]]
       ^{:key idx} [:div (u/at :classes (list 'il/neu-list-row
                                              'il/neu-list-row--no-padding))
                    [bl/block-link :href (path->uri (conj (get id->path id) target))
                     :sub-items [[:div (u/at :classes (list 'il/neu-list-column
                                                            'il/neu-list-column--grow
                                                            'il/neu-list-column--left
                                                            'il/neu-list-column--left-padded)) [truncated-string target]]
                                 [:div (u/at :classes (list 'il/neu-list-column
                                                            'il/neu-list-column--width-l)) [badged-text :bad c]]]]
                    #_[:div.list-column.list-column--grow.list-column--stretch.list-column--left
                     [:a.custom-block-link {:href (path->uri (conj (get id->path id) target))} [:span target]]]
                    #_[:div.list-column.list-column--width-l [badged-text :bad c]]])

     [:div.vertical-block
      [:div.list-row.list-row--border-less.list-row--no-padding.list-row--height-s
       [:div.list-column.list-column--grow]
       [:div.list-row__group
        [:div..list-column.list-column--clickable.list-column--stretch.list-column--separator {:on-click extend-limit} "More"]
        [:div..list-column.list-column--clickable.list-column--stretch.list-column--separator {:on-click unlim} "All"]]]]]))


(defn fail-type-slice-view [{:keys [nav-position-a struct runs fail-mapping test-data-map]}]
  (let [cache (atom {})

        ;combine-maps (fn [m [k v]]
        ;               (let [old-v (get m k 0)
        ;                     new-v (+ old-v v)]
        ;                 (assoc m k new-v)))

        _ (log-o "test-data-map" test-data-map)
        id->path (reduce (fn [coll [path {id :id category :category}]] (assoc coll id (into [category] path))) {} test-data-map)
        _ (log-o "id->path" id->path)
        ;id->fails (reduce (fn [coll [path {id :id summary :summary}]] (assoc coll id (get summary :fails)) ) {} test-data-map)
        ;_ (log-o "id->fails" id->fails)

        update-data-map (fn [id count coll path]
                          (let [{c :count i :ids} (get coll path {:count 0 :ids nil})
                                new-c (+ c count)
                                new-i (conj i id)]
                            (assoc coll path {:count new-c :ids new-i})))

        mk-data-map (fn [coll [fails id]]
                      (let [fails-sep (reduce conj [] (map (partial map identity) fails))
                            ;_ (log-o "fails sep " fails-sep)
                            fails-path (reduce (fn [coll [k v]]
                                                 (conj coll [(string/split (name k) #"\.") v])) [] fails-sep)
                            ;_ (log-o "fails path " fails-path)
                            path-branched (map (fn [[p k]] [(s/create-branch p) k]) fails-path)
                            ;_ (log-o "path-branched " path-branched)
                            data-map (reduce (fn [coll [paths count]]
                                               (reduce (partial update-data-map id count) coll paths)) coll path-branched)
                            ;_ (log-o "data-map " data-map)
                            ]
                        data-map))

        keyword->str (fn [x]
                       (if (keyword? x)
                         (subs (str x) 1)
                         x))

        mk-fail-tree (fn [path]
                       (let [scens-path (map rest (s/find-leaf struct path))
                             scens (map #(get test-data-map (flatten %)) scens-path)
                             scen-ids (map :id scens)
                             fails-keyworded (map #(get-in % [:summary :fails]) scens)
                             _ (log-o "fails keyworded " fails-keyworded)
                             fails (map #(map vector (map keyword->str (keys %)) (vals %)) fails-keyworded)
                             fails-keys (map keys fails)
                             _ (log-o "fail keys " fails-keys)
                             fails-combi (reduce into #{} fails-keys)
                             fails-paths (map #(string/split (keyword->str %) #"\.") fails-combi)
                             nested-fails-map (reduce #(assoc-in %1 %2 :leaf) {} fails-paths)
                             _ (log-o "nested-fails-map " nested-fails-map)
                             final-map (s/collapse-poor-branches nested-fails-map)
                             ;_ (log-o "final map" final-map)

                             ;fails-sep (reduce into [] (map (partial map identity) fails))
                             ;fails-map (reduce combine-maps {}  fails-sep)
                             ;fails-paths-map (reduce (fn [coll [k v]]
                             ;                          (conj coll [(string/split (name k) #"\.") v])) [] fails-map)
                             ;nested-fails-map (reduce (fn [coll [k v]]
                             ;                           (assoc-in coll k v)) {} fails-paths-map)
                             ;final-map (s/collapse-poor-branches nested-fails-map)
                             _ (log-o "test data map" test-data-map)
                             _ (log-o "scens path" scens-path)
                             _ (log-o "scens " scens)
                             _ (log-o "scens-ids " scen-ids)
                             _ (log-o "fails-map nested" nested-fails-map)
                             _ (log-o "final map" final-map)

                             fails-n-ids (map vector fails scen-ids)
                             data-map (reduce mk-data-map {} fails-n-ids)
                             _ (log-o "data-map " data-map)
                             ]
                         {:struct   final-map               ;
                          :data-map data-map}))

        decode-path-item (fn [x]
                           (if (re-find #"\." x)
                             (string/split x #"\.")
                             x))

        encode-path (fn [x]
                      (let [join-item (fn [x]
                                        (if (vector? x)
                                          (string/join "." x)
                                          x))]
                        (string/join "|" (mapv join-item x))))


        params->str (fn [x]
                      (string/join "&" (map (fn [[k v]] (str (name k) "=" v)) x)))

        mk-href (fn [target-path params fail-path]
                  (let [fail-path-encoded (encode-path fail-path)
                        new-params (assoc params :path fail-path-encoded)]
                    (str (path->uri target-path) "?" (params->str new-params))))

        decode-path (fn [x]
                      (let [x' (string/split x #"\|")
                            x'' (map decode-path-item x')]
                        (vec x'')))

        fail-path->str (fn [x]
                         (log-o "x" x)
                         (string/replace x #"\|" "."))
        ]

    (r/create-class {:component-did-update trigger-refresh-scroll
                     :component-did-mount  trigger-refresh-scroll
                     :component-function   (fn []
                                             (let [nav-pos @nav-position-a
                                                   path (routing/get-path nav-pos)
                                                   _ (log-o "path vec?" (vector? path))
                                                   params (routing/get-query-params nav-pos)
                                                   cached-fail-tree (get @cache path)
                                                   fail-tree (if-not cached-fail-tree
                                                               (let [tree (mk-fail-tree path)
                                                                     _ (swap! cache assoc path tree)]
                                                                 tree)
                                                               cached-fail-tree)
                                                   last-elem (peek (flatten-path path))
                                                   _ (log-o "fail-tree" fail-tree)
                                                   path-encoded (get params :path)
                                                   fail-path (decode-path path-encoded)
                                                   _ (log-o "path decoded" fail-path)
                                                   leaf? (= :leaf (get-in fail-tree (into [:struct] fail-path)))
                                                   _ (log-o "leaf?" leaf?)
                                                   ids (get-in fail-tree [:data-map (flatten fail-path) :ids])
                                                   _ (log-o "ids" ids)
                                                   caption (if leaf?
                                                             (fail-path->str path-encoded)
                                                             (str "Fail type slice: " last-elem))
                                                   _ (log-o "caption" caption)
                                                   ]
                                               ;(log "slice")
                                               [:div
                                                [:div.list-row.list-row--height-xl.list-row--border-less.list-row--m-bottom-m.list-row--no-padding
                                                 [:div.list-column.list-column--grow.list-column--left
                                                  [:h1.margin-less caption]]]
                                                (if leaf?
                                                  [fails-leaf {:fail-mapping fail-mapping
                                                               :ids          ids
                                                               :id->path     id->path
                                                               :runs         runs
                                                               :fail-name    (fail-path->str path-encoded)}]
                                                  [fails-node fail-tree fail-path (partial mk-href path params)])]))})))


(defn app-content [{:keys [test-data-map quarantine summary-map runs struct status-map fail-mapping status-filter-a nav-position-a]}]
  (r/create-class
    {
     :component-did-mount (fn [this]
                            ;(log "******* mounted ********")
                            (.on (js/$ (r/dom-node this)) "refresh-scroll" #(.resize (.getNiceScroll (js/$ (r/dom-node this)))))
                            (.niceScroll (js/$ (r/dom-node this))))
     :component-function  (fn []
                            (let [nav-pos @nav-position-a
                                  params (routing/get-query-params nav-pos)
                                  path (routing/get-path nav-pos)
                                  fail-type-slice (= "failtype" (get params :slice))]
                              ;(log "app-content rerendered")
                              ;(log-o "path " path)
                              [:div (u/attr {:classes 'cp/content-pane})
                               (if fail-type-slice
                                 [fail-type-slice-view {:nav-position-a nav-position-a
                                                        :struct         struct
                                                        :runs           runs
                                                        :fail-mapping   fail-mapping
                                                        :test-data-map  test-data-map}]
                                 (cond
                                   (= path []) [home-view {:struct          struct
                                                           :runs            runs
                                                           :test-data-map   test-data-map
                                                           :status-map      status-map
                                                           :status-filter-a status-filter-a}]
                                   (is-node? struct path) [node-view {:struct          struct
                                                                      :runs            runs
                                                                      :summary-map     summary-map
                                                                      :test-data-map   test-data-map
                                                                      :status-map      status-map
                                                                      :status-filter-a status-filter-a
                                                                      :nav-position-a  nav-position-a}]
                                   (is-scenario? struct path) (let [scenario-info (get test-data-map (rest (flatten-path path)))
                                                                    runs-id (get scenario-info :id)
                                                                    scen-quarantine (get quarantine runs-id)
                                                                    ;_ (log-o "runs-id " runs-id)
                                                                    ]
                                                                [scenario-view {:scenario-name       (path->str (peek path))
                                                                                :get-runs-fn         #(get runs runs-id)
                                                                                :scen-quarantine     scen-quarantine
                                                                                :scenario-status-map (get status-map (flatten-path path))
                                                                                :status-filter-a     status-filter-a
                                                                                :scenario-info       scenario-info
                                                                                :path                path}])
                                   (is-run? test-data-map path) (let [scenario-path (pop path)
                                                                      target (peek path)
                                                                      scenario-info (get test-data-map (rest (flatten-path scenario-path)))
                                                                      runs-id (get scenario-info :id)
                                                                      scen-quarantine (get quarantine runs-id)
                                                                      runs (get runs runs-id)
                                                                      run (get runs target)
                                                                      doc-strings (string/split (get scenario-info :doc) #"\n\n")]
                                                                  [run-view target scen-quarantine run doc-strings])
                                   :else nil))]))}))