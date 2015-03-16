(ns report.main
  (:require [reagent.core :as r]
            [report.components.app :refer [app]]
            [report.routing :as routing]
            [report.components.app-content :refer [app-content-nicescroll]]
            [report.components.app-bar :refer [app-bar]]
            [report.utils.log :refer [log]]
            [jquery.main]
            [jquery.nicescroll]
            [report.routing :as routing])
  )





(r/render-component [app (app-bar (fn [x]
                                    (let [c (count x)] (if (odd? c) :good :bad))) routing/nav-position) app-content-nicescroll]
                    (.getElementById js/document "app"))

