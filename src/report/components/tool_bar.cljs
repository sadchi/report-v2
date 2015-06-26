(ns report.components.tool-bar
  (:require [report.components.common.utils :as u]
            [report.components.styles.tool-bar :as t]
            [report.utils.log :refer [log log-o]]))


(defn tool-bar-label [s]
  (fn []
    [:span (u/attr {:classes 't/tool-bar__item}) s]))


(defn tool-bar-action-label [{:keys [text is-active-f? get-href-f]}]
  (fn []
    #_(log "tool-bar-action-label")
    (let [active? (is-active-f?)
          ;_ (log-o "active?" active?)
          main-classes (list 't/tool-bar__item 't/tool-bar__item--clickable)
          class (if active?
                  main-classes
                  (conj main-classes 't/tool-bar__item--disabled))]
      [:div (u/attr {:classes  class}) [:a {:href (get-href-f)} text]])))

(defn tool-bar [& sub-items]
  (fn []
    [:div (u/attr {:classes 't/tool-bar})
     (for [[idx item] (map-indexed vector sub-items)]
       ^{:key idx} [item])]))