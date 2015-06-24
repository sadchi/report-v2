(ns report.components.tool-bar
  (:require [report.components.common.utils :as u]
            [report.components.styles.tool-bar :as t]))


(defn tool-bar []
  (fn []
    [:div (u/attr {:classes 't/tool-bar}) "TEST"]))