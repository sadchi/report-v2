(ns report.components.app
  (:require [report.components.common.utils :as u :refer [add-style!]]
            [report.components.styles.main-containers :as mc]
            [report.components.app-bar :refer [app-bar]]))

(defn app [& sub-items]
  (fn []
    [:div (u/attr {:classes 'mc/app})
     (for [[idx item] (map-indexed vector sub-items)] ^{:key idx} [item])]))
