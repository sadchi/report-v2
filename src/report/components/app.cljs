(ns report.components.app
  (:require [report.components.app-bar :refer [app-bar]]
            [report.components.app-content :refer [app-content app-content-nicescroll]]))

(defn app [test-results-data]
  (fn []
    [:div.app
     [app-bar]
     [app-content-nicescroll]]))
