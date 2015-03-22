(ns report.components.app-content
  (:require [reagent.core :as r]
            [report.test-results.statuses :refer [sort-statuses]]
            [report.utils.log :refer [log log-o]]))





(defn- list-status-counts [status-map status-list]
  (for [[idx status] (map-indexed vector status-list)]
    ^{:key idx} [:div.list-column (get status-map (keyword status) "-")]))

(defn- category [cat-name struct status-map]
  )


(defn home-view [struct status-map]
  (let [root-statuses (get status-map [])
        statuses-unsorted (map name (keys root-statuses))
        statuses (sort-statuses < statuses-unsorted)
        categories (keys struct)
        _ (log-o "cats " categories)]
    (fn []
     [:div
      [:div.list-caption "Overview"]
      [:div.list-row.list-row--accent
       [:div.list-column.list-column--grow.list-column--left "Total"]
       (list-status-counts (get status-map []) statuses)
       #_(for [[idx status] (map-indexed vector statuses)]
         ^{:key idx} [:div.list-column (root-status-count status)])]
      (for [cat-indexed (map-indexed vector categories)
            :let [[idx cat] cat-indexed
                  cat-status-map (get status-map [cat])]]
        ^{:key idx} [:div.list-row.list-row--accent
                     [:div.list-column.list-column--grow.list-column--left cat]
                     (list-status-counts cat-status-map statuses)])])))


(defn app-content [struct status-map]
  (fn []
    [:div.content-pane
     [home-view struct status-map]
     [:a {:href "#/234"} "TEST1"] [:a {:href "#/123/234"} "TEST2"] [:a {:href "#/123/234/34534"} "TEST3"]
     [:a {:href "#/123/234/123/1dd/dfg"} "TEST4"]]))

(def app-content-nicescroll
  (with-meta app-content
             {:component-did-mount #(.niceScroll (js/$ (r/dom-node %)))}))