(ns report.main
  (:require [reagent.core :as r]
            [report.test-results.structure :as structure]
            [report.components.app :refer [app]]
            [report.routing :as routing]
            [report.components.app-content :refer [app-content-nicescroll]]
            [report.components.app-bar :refer [app-bar]]
            [report.utils.log :refer [log log-o]]
            [jquery.main]
            [jquery.nicescroll]
            [report.routing :as routing])
  )



(def test-data (structure/build-map-by (js->clj js/data :keywordize-keys true) :path))

(log-o "td: " test-data)

(def test-data-structure
  (-> (structure/mk-tree (keys test-data))
      (structure/collapse-poor-branches)))

(log-o "td-struct: " test-data-structure)

(def status-map (structure/mk-status-lists test-data #(get % :status)))


(log-o "status map: " status-map)

(r/render-component [app (app-bar (fn [x] (condp = (count x)
                                            0 "SUCCESS"
                                            1 "SUCCESS"
                                            2 "FAIL"
                                            3 "UNDEF"
                                            4 "SUCCESS"
                                            "UNDEF")) routing/nav-position) app-content-nicescroll]
                    (.getElementById js/document "app"))

