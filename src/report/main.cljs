(ns report.main
  (:require [reagent.core :as r]
            [report.components.app :refer [app]]
            [jquery.main]
            [jquery.nicescroll]))


(r/render-component [app nil] (.getElementById js/document "app"))

