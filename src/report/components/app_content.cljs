(ns report.components.app-content
  (:require [reagent.core :as r]
            [report.test-results.statuses :refer [sort-statuses sort-keyworded-statuses bad-status? good-status? neutral-status? get-worse get-best]]
            [report.test-results.structure :refer [get-assets leaf-content is-that-run? is-node? is-scenario? is-run?]]
            [report.components.badges :refer [badged-text]]
            [report.components.runs-meta :refer [meta-data-render]]
            [report.components.fails-list :refer [fails-list]]
            [report.components.errors-list :refer [errors-list]]
            [report.components.assets-list :refer [assets-list]]
            [report.utils.log :refer [log log-o]]
            [report.utils.net :refer [set-href!]]
            [report.routing :refer [path->uri]]
            [report.components.status-filter :refer [status-filter active? any-active?]]
            [report.test-results.path :refer [flatten-path path->str]]
            [report.components.buttons :refer [state-button button]]
            [clojure.string :as string]))




(def default-runs-limit 40)
(def default-runs-more-count 10)


(defn- list-row-status-names [{:keys [text statuses accent]}]
  (let [status-names (map name statuses)
        accent-class (when accent "list-row--accent")]
    [:div.list-row {:class accent-class}
     [:div.list-column.list-column--grow.list-column--left text]
     (for [[idx status] (map-indexed vector status-names)]
       ^{:key idx} [:div.list-column status])]))


(defn- list-row [{:keys [text statuses parent-statuses status-filter-a href accent]}]
  (let [status-names (map name (keys statuses))
        worse-status (get-worse status-names)
        best-status (get-best status-names)
        ;_ (log-o "worse-status " worse-status)
        status-class (cond
                       (good-status? worse-status) "list-row--success"
                       (bad-status? worse-status) "list-row--error"
                       (and (neutral-status? worse-status) (good-status? best-status)) "list-row--success"
                       :else "")
        ;_ (log-o "status-class " status-class)

        accent-class (when accent "list-row--accent")
        ;style (when href {:cursor "pointer"})
        extra-classes (str accent-class " " status-class)
        vis (if (nil? status-filter-a)
              true
              (any-active? (keys statuses) @status-filter-a))]
    (when vis [:div.list-row {:class extra-classes}
               [:div.list-column.list-column--grow.list-column--left
                [:a.custom-link.custom-link--full-width {:href href} text]]
               (for [[idx status] (map-indexed vector parent-statuses)
                     :let [status-count (get statuses status nil)]]
                 (if status-count
                   ^{:key idx} [:div.list-column [badged-text status status-count]]
                   ^{:key idx} [:div.list-column]))])))



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

(defn- gen-uri-jumps [{:keys [test-data-map struct parent-path]}]
  ;(log "gen-uri-jumps called")
  ;(log-o "test-data-map " test-data-map)
  (let [get-single-run-target (fn [runs]
                                (if (= 1 (count runs))
                                  (get (first runs) :target)
                                  nil))
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
                                             runs (get scen-info :runs)
                                             ;_ (log-o "runs " runs)
                                             single-target (get-single-run-target runs)
                                             ;_ (log-o "single-target " single-target)
                                             ]
                                         (if single-target
                                           (conj full-path single-target)
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

(defn- category [{:keys [cat-name struct test-data-map status-map parent-statuses status-filter-a]}]
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
                                                                      :parent-path   cat-path})}]])))))


(defn home-view [{:keys [struct test-data-map status-map status-filter-a]}]
  (let [root-status-map (get status-map [])
        statuses (sort-keyworded-statuses > (keys root-status-map))
        categories (keys struct)
        ;_ (log-o "cats " categories)
        ]
    (fn []
      [:div
       [:div.list-caption
        [:div.list-column.list-column--grow.list-column--left [:h1.margin-less "Overview"]]
        [status-filter statuses root-status-map status-filter-a]]

       [list-row-status-names {:text     "Path:"
                               :statuses statuses
                               :accent   true}]
       (for [cat-indexed (map-indexed vector categories)
             :let [[idx cat] cat-indexed]]
         ^{:key idx} [category {:cat-name        cat
                                :struct          struct
                                :test-data-map   test-data-map
                                :status-map      status-map
                                :parent-statuses statuses
                                :status-filter-a status-filter-a}])
       ])))


(defn node-view [{:keys [struct test-data-map status-map status-filter-a nav-position-a]}]
  (fn []
    (let [path @nav-position-a
          node-title (path->str (peek path))
          node-status-map (get status-map (flatten-path path))
          statuses (sort-keyworded-statuses > (keys node-status-map))
          ;_ (log "node-view rendered")
          ;_ (log-o "statuses " statuses)
          ;_ (log-o "node-map " node-status-map)
          sub-items (keys (get-in struct path))
          ;_ (log-o "sub-items " sub-items)
          ]
      [:div
       [:div.list-caption
        [:div.list-column.list-column--grow.list-column--left [:h1.margin-less node-title]]
        [status-filter statuses node-status-map status-filter-a]]
       [list-row-status-names {:text     "Path:"
                               :statuses statuses
                               :accent   true}]
       [sub-struct-list {:status-map      status-map
                         :sub-items       sub-items
                         :parent-statuses statuses
                         :parent-path     path
                         :status-filter-a status-filter-a
                         :get-href-fn     (gen-uri-jumps {:test-data-map test-data-map
                                                          :struct        struct
                                                          :parent-path   path})}]])))



(defn- extract-target-status [runs limit active?]
  (let [
        f (fn [coll x]
            (let [{:keys [target status]} x
                  old-list (get coll :list)
                  old-limit-remains (get coll :limit-remains)]
              (if (and (active? status) (pos? old-limit-remains))
                (-> coll
                    (assoc :list (conj old-list [target status]))
                    (assoc :limit-remains (dec old-limit-remains)))
                coll)))]
    (-> (reduce f {:list [] :limit-remains limit} runs)
        (get :list))))

(defn- scenario-runs [runs status-filter-a runs-limit path]
  (r/create-class
    {:component-did-update (fn [this]
                             (.trigger (js/$ (r/dom-node this)) "refresh-scroll"))
     :component-function   (fn []
                             (let [target-status-coll (extract-target-status runs @runs-limit #(active? % status-filter-a))
                                   ;_ (log-o "target-statuses: " target-status-coll)
                                   ]
                               [:div
                                [:div.list-row.list-row--accent
                                 [:div.list-column.list-column--grow.list-column--left "Target"]
                                 [:div.list-column "Status"]]
                                (for [[idx target-status] (map-indexed vector target-status-coll)
                                      :let [[target status] target-status]]
                                  ^{:key idx} [:div.list-row
                                               [:div.list-column.list-column--grow.list-column--left
                                                [:a.custom-link.custom-link--full-width {:href (path->uri (conj path target))} target]]
                                               [:div.list-column [badged-text status status]]])]))}))


(defn doc [doc-strings]
  (when-not (empty? doc-strings)
    [:div.vertical-block
     (for [[idx str] (map-indexed vector doc-strings)]
       ^{:key idx} [:p str])]))

(defn- scenario-view [{:keys [scenario-info scenario-name scenario-status-map status-filter-a path]}]
  (let [runs (get scenario-info :runs)
        runs-limit (r/atom default-runs-limit)
        extend-limit #(swap! runs-limit (partial + default-runs-more-count))
        unlim #(reset! runs-limit (count runs))]
    (fn []
      (let [statuses (sort-keyworded-statuses > (keys scenario-status-map))
            doc-strings (string/split (get scenario-info :doc) #"\n\n")
            ;_ (log-o "scenario-info: " scenario-info)
            ;_ (log-o "doc strings: " doc-strings)
            ]
        [:div
         [:div.list-caption
          [:div.list-column.list-column--grow.list-column--left [:h1.margin-less scenario-name]]
          [status-filter statuses scenario-status-map status-filter-a]]
         [doc doc-strings]
         [scenario-runs runs status-filter-a runs-limit path]
         [:div.vertical-block
          [:div.list-row.list-row--border-less.list-row--no-padding
           [:div.list-column.list-column--grow.list-column--right
            [:div
             [button extend-limit "More"] [button unlim "All"]]]]]]))))



(defn run-view [run doc-strings]
  (fn []
    (let [target (get run :target)
          meta-data (get run :meta)]
      [:div
       [:div.list-caption
        [:div.list-column.list-column--grow.list-column--left [:h1.margin-less target]]]
       [doc doc-strings]
       [meta-data-render meta-data]
       [fails-list (get run :fails)]
       [errors-list (get run :errors)]
       [assets-list (get-assets run)]
       ])))


(defn app-content [{:keys [test-data-map struct status-map status-filter-a nav-position-a]}]
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
                              [:div.content-pane
                               (cond
                                 (= path []) [home-view {:struct          struct
                                                         :test-data-map   test-data-map
                                                         :status-map      status-map
                                                         :status-filter-a status-filter-a}]
                                 (is-node? struct path) [node-view {:struct          struct
                                                                    :test-data-map   test-data-map
                                                                    :status-map      status-map
                                                                    :status-filter-a status-filter-a
                                                                    :nav-position-a  nav-position-a}]
                                 (is-scenario? struct path) [scenario-view {:scenario-name       (path->str (peek path))
                                                                            :scenario-status-map (get status-map (flatten-path path))
                                                                            :status-filter-a     status-filter-a
                                                                            :scenario-info       (get test-data-map (rest (flatten-path path)))
                                                                            :path                path}]
                                 (is-run? struct path) (let [scenario-path (pop path)
                                                             target (peek path)
                                                             scenario-info (get test-data-map (rest (flatten-path scenario-path)))
                                                             runs (get scenario-info :runs)
                                                             run (first (filter (partial is-that-run? target) runs))
                                                             doc-strings (string/split (get scenario-info :doc) #"\n\n")]
                                                         [run-view run doc-strings])
                                 :else nil)]))}))

