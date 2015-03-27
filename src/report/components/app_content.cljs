(ns report.components.app-content
  (:require [reagent.core :as r]
            [report.test-results.statuses :refer [sort-statuses bad-status? good-status? neutral-status? get-worse get-best]]
            [report.components.badges :refer [badged-count]]
            [report.utils.log :refer [log log-o]]
            [report.utils.net :refer [set-href!]]
            [report.routing :refer [path->uri]]
            [report.components.status-filter :refer [status-filter any-active?]]
            [report.test-results.path :refer [flatten-path path->str]]
            [report.components.state-button :refer [state-button]]))




(defn- list-row-status-names [{:keys [text statuses accent]}]
  (let [status-names (map name statuses)
        accent-class (when accent "list-row--accent")]
    (fn []
      [:div.list-row {:class accent-class}
       [:div.list-column.list-column--grow.list-column--left text]
       (for [[idx status] (map-indexed vector status-names)]
         ^{:key idx} [:div.list-column status])])))


(defn- list-row [{:keys [text statuses parent-statuses status-filter-a on-click-fn accent]}]
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
        style (when on-click-fn {:cursor "pointer"})
        extra-classes (str accent-class " " status-class)]
    (fn []
      (let [vis (if (nil? status-filter-a)
                  true
                  (any-active? (keys statuses) @status-filter-a))]
        (when vis [:div.list-row {:class extra-classes :on-click on-click-fn :style style}
                   [:div.list-column.list-column--grow.list-column--left text]
                   (for [[idx status] (map-indexed vector parent-statuses)
                         :let [status-count (get statuses status nil)]]
                     (if status-count
                       ^{:key idx} [:div.list-column [badged-count status status-count]]
                       ^{:key idx} [:div.list-column]))])))))

(defn- category [{:keys [cat-name struct status-map parent-statuses status-filter-a]}]
  (let [cat-statuses (get status-map [cat-name])
        ;_ (log-o "cat-statuses " cat-statuses)
        sub-items (keys struct)
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
                              :accent          true}]
                   (for [[idx item] (map-indexed vector sub-items)
                         :let [item-statuses (get status-map (flatten-path [cat-name item]))]]
                     ^{:key idx} [list-row {:text            (path->str item)
                                            :statuses        item-statuses
                                            :parent-statuses parent-statuses
                                            :status-filter-a status-filter-a
                                            :on-click-fn #(set-href! (path->uri [cat-name item]))
                                            :accent          false}])])))))


(defn home-view [struct status-map status-filter-a]
  (let [root-statuses (get status-map [])
        statuses-unsorted (map name (keys root-statuses))
        statuses (map keyword (sort-statuses > statuses-unsorted))
        categories (keys struct)
        ;_ (log-o "cats " categories)
        ]
    (fn []
      [:div
       [:div.list-caption
        [:div.list-column.list-column--grow.list-column--left [:h1 "Overview"]]
        [status-filter statuses root-statuses status-filter-a]]

       [list-row-status-names {:text     "Category:"
                               :statuses statuses
                               :accent   true}]
       #_[list-row {:text            "Total"
                  :statuses        (get status-map [])
                  :parent-statuses statuses
                  :status-filter-a nil
                  :accent          false}]
       (for [cat-indexed (map-indexed vector categories)
             :let [[idx cat] cat-indexed]]
         ^{:key idx} [category {:cat-name        cat
                                :struct          (get struct cat)
                                :status-map      status-map
                                :parent-statuses statuses
                                :status-filter-a status-filter-a}])
       ])))


(defn app-content [struct status-map status-filter-a]
  (r/create-class
    {
     :component-did-mount (fn [this]
                            ;(log "******* mounted ********")
                            (.niceScroll (js/$ (r/dom-node this))))
     :component-function  (fn []
                            [:div.content-pane
                             [home-view struct status-map status-filter-a]
                             [:a {:href "#/234"} "TEST1"] [:a {:href "#/123/234"} "TEST2"] [:a {:href "#/123/234/34534"} "TEST3"]
                             [:a {:href "#/123/234/123/1dd/dfg"} "TEST4"]])}))


(def app-content-nicescroll
  (with-meta app-content
             {:component-did-mount (fn [this]
                                     (log "******* mounted ********")
                                     (.niceScroll (js/$ (r/dom-node this))))}))