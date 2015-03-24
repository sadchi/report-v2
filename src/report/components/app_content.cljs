(ns report.components.app-content
  (:require [reagent.core :as r]
            [report.test-results.statuses :refer [sort-statuses bad-status? good-status? neutral-status? get-worse get-best]]
            [report.utils.log :refer [log log-o]]
            [report.test-results.path :refer [flatten-path path->str]]))


(defn- badged-count [status cnt]
  (let [named-status (name status)
        class (cond
                (good-status? named-status) "success-back-theme"
                (bad-status? named-status) "error-back-theme"
                :else "neutral-back-theme")
        _ (log "status: " named-status)]
    (fn []
      [:div.badge {:class class} cnt])))

(defn- list-row [{:keys [text statuses parent-statuses status-filter-a accent]}]
  (let [status-names (map name (keys statuses))
        worse-status (get-worse status-names)
        best-status (get-best status-names)
        _ (log-o "worse-status " worse-status)
        status-class (cond
                       (good-status? worse-status) "list-row--success"
                       (bad-status? worse-status) "list-row--error"
                       (and (neutral-status? worse-status) (good-status? best-status)) "list-row--success"
                       :else "")
        _ (log-o "status-class " status-class)

        accent-class (when accent "list-row--accent")
        extra-classes (str accent-class " " status-class)]
    (fn []
     [:div.list-row {:class extra-classes}
      [:div.list-column.list-column--grow.list-column--left text]
      (for [[idx status] (map-indexed vector parent-statuses)
            :let [status-count (get statuses status nil)]]
        (if status-count
          ^{:key idx} [:div.list-column [badged-count status status-count]]
          ^{:key idx} [:div.list-column]))])))

(defn- category [{:keys [cat-name struct status-map parent-statuses status-filter-a]}]
  (let [cat-statuses (get status-map [cat-name])
        _ (log-o "cat-statuses " cat-statuses)
        sub-items (keys struct)
        _ (log-o "sub-items " sub-items)]
    (fn []
     [:div
      [list-row {:text cat-name
                 :statuses cat-statuses
                 :parent-statuses parent-statuses
                 :status-filter-a status-filter-a
                 :accent true}]
      (for [[idx item] (map-indexed vector sub-items)
            :let [item-statuses (get status-map (flatten-path [cat-name item]))]]
        ^{:key idx} [list-row {:text (path->str item)
                               :statuses item-statuses
                               :parent-statuses parent-statuses
                               :status-filter-a status-filter-a
                               :accent false}])])))


(defn home-view [struct status-map]
  (let [root-statuses (get status-map [])
        statuses-unsorted (map name (keys root-statuses))
        statuses (map keyword (sort-statuses > statuses-unsorted))
        categories (keys struct)
        _ (log-o "cats " categories)]
    (fn []
     [:div
      [:div.list-caption "Overview"]
      [list-row {:text "Total"
                 :statuses (get status-map [])
                 :parent-statuses statuses
                 :status-filter-a nil
                 :accent true}]
      (for [cat-indexed (map-indexed vector categories)
            :let [[idx cat] cat-indexed]]
        ^{:key idx} [category {:cat-name   cat
                               :struct     (get struct cat)
                               :status-map status-map
                               :parent-statuses statuses
                               :status-filter-a nil}])])))


(defn app-content [struct status-map]
  (fn []
    [:div.content-pane
     [home-view struct status-map]
     [:a {:href "#/234"} "TEST1"] [:a {:href "#/123/234"} "TEST2"] [:a {:href "#/123/234/34534"} "TEST3"]
     [:a {:href "#/123/234/123/1dd/dfg"} "TEST4"]]))

(def app-content-nicescroll
  (with-meta app-content
             {:component-did-mount #(.niceScroll (js/$ (r/dom-node %)))}))