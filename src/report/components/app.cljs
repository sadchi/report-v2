(ns report.components.app
  (:require [report.components.app-bar :refer [app-bar]]))

(defn app [& sub-items]
  (fn []
    [:div.app
     (for [[idx item] (map-indexed vector sub-items)] ^{:key idx} [item])]))
