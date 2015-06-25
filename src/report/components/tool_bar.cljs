(ns report.components.tool-bar
  (:require [report.components.common.utils :as u]
            [report.components.styles.tool-bar :as t]))


(defn tool-bar-label [s]
  (fn []
    [:span (u/attr {:classes 't/tool-bar__item}) s]))


(defn tool-bar [& sub-items]
  (fn []
    [:div (u/attr {:classes 't/tool-bar})
     (for [[idx item] (map-indexed vector sub-items)]
       ^{:key idx} [item])]))