(ns report.components.app-content
  (:require [reagent.core :as r]
            [report.test-results.statuses :refer [get-reputation sort-statuses-by-weight sort-statuses-by-vis-order bad-status? good-status? neutral-status? get-worse get-best]]
            [report.test-results.structure :refer [get-assets leaf-content is-that-run? is-node? is-scenario? is-run? get-runs-count]]
            [report.test-results.extra-params :refer [build-name]]
            [report.components.badges :refer [badged-text]]
            [report.components.runs-meta :refer [meta-data-render]]
            [report.components.fails-list :refer [fails-list]]
            [report.components.errors-list :refer [errors-list]]
            [report.components.assets-list :refer [assets-list]]
            [report.components.truncated-string :refer [truncated-string]]
            [report.components.tooltip :as tooltip]
            [report.components.dropdown :refer [dropdown]]
            [report.components.styles.content-pane :as cp]
            [report.components.common.utils :as u]
            [report.utils.log :refer [log log-o]]
            [report.utils.net :refer [set-href!]]
            [report.routing :refer [path->uri]]
            [report.components.status-filter :refer [status-filter hovered? w-a-active? active? any-active?]]
            [report.test-results.path :refer [safe-path desafe-path flatten-path path->str]]
            [report.components.buttons :refer [state-button button]]
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


(defn- list-row-status-names [{:keys [text statuses accent status-filter-a]}]
  (let [status-names (map name statuses)
        accent-class (when accent "list-row--accent")
        status-filter @status-filter-a]
    [:div.list-row {:class accent-class}
     [:div.list-column.list-column--grow.list-column--left text]
     (for [[idx status] (map-indexed vector status-names)
           :let [
                 ;_ (log-o "status " status)
                 active (w-a-active? status status-filter)
                 ;_ (log-o "active " active)
                 class (when-not active "list-column--shadowed")
                 ]]
       ^{:key idx} [:div.list-column (merge {:class class} (mk-tooltip-map status :center)) status])]))


(defn- list-row [{:keys [text statuses parent-statuses status-filter-a href accent]}]
  (let [
        status-names (map name (keys statuses))
        ;_ (log "1")
        worse-status (get-worse status-names)
        ;_ (log "2")
        best-status (get-best status-names)
        ;_ (log-o "worse-status " worse-status)
        ;_ (log "3")
        status-class nil
        #_(cond
                       (good-status? worse-status) "list-row--success"
                       (bad-status? worse-status) "list-row--error"
                       (and (neutral-status? worse-status) (good-status? best-status)) "list-row--success"
                       :else "")
        ;_ (log-o "status-class " status-class)

        accent-class (when accent "list-row--accent")
        ;_ (log "4")
        ;style (when href {:cursor "pointer"})
        extra-classes (str accent-class " " status-class)
        ;_ (log "4")
        status-filter (when status-filter-a @status-filter-a)
        ;_ (log "5")
        vis (any-active? (keys statuses) status-filter)
        ;_ (log "6")
        ]
    (when vis [:div.list-row.list-row--hoverable {:class extra-classes}
               [:div.list-column.list-column--grow.list-column--stretch.list-column--left
                [:a.custom-block-link {:href href} [:span text]]]
               (for [[idx status] (map-indexed vector parent-statuses)
                     :let [status-count (get statuses status nil)
                           active (w-a-active? status status-filter)
                           hover (hovered? status status-filter)
                           active-status (when-not active "list-column--shadowed")
                           hover-status (when hover "list-column--hovered")
                           class (str active-status " " hover-status)]]
                 (if status-count
                   ^{:key idx} [:div.list-column {:class class} [badged-text (get-reputation status) status-count]]
                   ^{:key idx} [:div.list-column {:class class}]))])))



(defn- sub-struct-list [{:keys [status-map sub-items parent-statuses parent-path status-filter-a get-href-fn]}]
  ;(log "sub-struct-list")
  ;(log-o "sub-items " sub-items)
  [:div
   (for [[idx item] (map-indexed vector sub-items)
         :let [
               ;_ (log-o "item " item)
               item-path (conj parent-path item)
               item-statuses (get status-map (flatten-path item-path))]]
     ^{:key idx} [list-row {:text            (path->str item)
                            :statuses        item-statuses
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
                                [:div.list-row.list-row--height-xl.list-row--border-less.list-row--m-bottom-m
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



(defn flat-list [{:keys [status-map parent-path items status-filter-a get-href-fn]}]
  (let [status-filter @status-filter-a]
    [:div
     [:div.list-row.list-row--accent
      [:div.list-column.list-column--grow.list-column--left "Path:"]
      [:div.list-column "Status"]]
     (for [[idx item] (map-indexed vector items)
           :let [full-path (flatten-path (conj parent-path item))
                 item-status-map (get status-map full-path)
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
         ^{:key idx} [:div.list-row.list-row--hoverable
                      [:div.list-column.list-column--grow.list-column--stretch.list-column--left #_{:class status-class}
                       [:a.custom-block-link {:href (get-href-fn item)} [:span str-item]]]
                      [:div.list-column.list-column--width-l [badged-text (get-reputation status) status]]]))]))

(defn node-view [{:keys [struct runs test-data-map status-map status-filter-a nav-position-a]}]
  (r/create-class
    {:component-did-update trigger-refresh-scroll
     :component-did-mount  trigger-refresh-scroll
     :component-function   (fn []
                             (let [path @nav-position-a
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
                               [:div
                                [:div.list-row.list-row--height-xl.list-row--border-less.list-row--m-bottom-m
                                 [:div.list-column.list-column--grow.list-column--left
                                  [:h1.margin-less [truncated-string node-title]]]
                                 (status-filter statuses node-status-map status-filter-a)]

                                (if-not flat-list?
                                  (list
                                    ^{:key 1} [:div.list-row.list-row--accent
                                               [:div.list-column.list-column--grow.list-column--left "Path:"]]

                                    ^{:key 2} [sub-struct-list {:status-map      status-map
                                                                :sub-items       sub-items
                                                                :parent-statuses statuses
                                                                :parent-path     path
                                                                :status-filter-a status-filter-a
                                                                :get-href-fn     (gen-uri-jumps {:test-data-map test-data-map
                                                                                                 :struct        struct
                                                                                                 :runs          runs
                                                                                                 :parent-path   path})}])
                                  [flat-list {:status-map      status-map
                                              :parent-path     path
                                              :items           sub-items
                                              :status-filter-a status-filter-a
                                              :get-href-fn     (gen-uri-jumps {:test-data-map test-data-map
                                                                               :runs          runs
                                                                               :struct        struct
                                                                               :parent-path   path})}])]))}))



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




(defn- scenario-runs [{:keys [get-runs-fn scen-quarantine status-filter-a runs-limit path]}]
  (r/create-class
    {:component-did-update trigger-refresh-scroll
     :component-function   (fn []
                             ;(log "scenario runs")
                             (let [target-status-coll (extract-target-status get-runs-fn scen-quarantine @runs-limit #(active? % status-filter-a))
                                   ;_ (log-o "target-statuses: " target-status-coll)
                                   ]
                               [:div
                                [:div.list-row.list-row--accent
                                 [:div.list-column.list-column--grow.list-column--left "Target"]
                                 [:div.list-column "Status"]]
                                (for [[idx target-status] (map-indexed vector target-status-coll)
                                      :let [[target status] target-status]]
                                  ^{:key idx} [:div.list-row.list-row--hoverable
                                               [:div.list-column.list-column--grow.list-column--stretch.list-column--left
                                                [:a.custom-block-link {:href (path->uri (conj path target))} [:span target]]]
                                               [:div.list-column.list-column--width-l [badged-text (get-reputation status) status]]])]))}))


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
            ;_ (log-o "scenario-info: " scenario-info)
            ;_ (log-o "doc strings: " doc-strings)
            ]
        [:div

         [:div.list-row.list-row--height-xl.list-row--border-less.list-row--m-bottom-m
          [:div.list-column.list-column--grow.list-column--left
           [:h1.margin-less [truncated-string scenario-name]]]
          (status-filter statuses scenario-status-map status-filter-a)]

         [doc doc-strings]
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


(defn app-content [{:keys [test-data-map quarantine runs struct status-map status-filter-a nav-position-a]}]
  (r/create-class
    {
     :component-did-mount (fn [this]
                            ;(log "******* mounted ********")
                            (.on (js/$ (r/dom-node this)) "refresh-scroll" #(.resize (.getNiceScroll (js/$ (r/dom-node this)))))
                            (.niceScroll (js/$ (r/dom-node this))))
     :component-function  (fn []
                            (let [path @nav-position-a]
                              ;(log "app-content rerendered")
                              ;(log-o "path " path)
                              [:div (u/attr {:classes 'cp/content-pane})
                               (cond
                                 (= path []) [home-view {:struct          struct
                                                         :runs            runs
                                                         :test-data-map   test-data-map
                                                         :status-map      status-map
                                                         :status-filter-a status-filter-a}]
                                 (is-node? struct path) [node-view {:struct          struct
                                                                    :runs            runs
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
                                 (is-run? struct path) (let [scenario-path (pop path)
                                                             target (peek path)
                                                             scenario-info (get test-data-map (rest (flatten-path scenario-path)))
                                                             runs-id (get scenario-info :id)
                                                             scen-quarantine (get quarantine runs-id)
                                                             runs (get runs runs-id)
                                                             run (get runs target)
                                                             doc-strings (string/split (get scenario-info :doc) #"\n\n")]
                                                         [run-view target scen-quarantine run doc-strings])
                                 :else nil)]))}))