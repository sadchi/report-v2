(ns report.main
  (:require [reagent.core :as r]
            [report.test-results.structure :as structure]
            [report.test-results.path :as path]
            [report.components.app :refer [app]]
            [report.components.status-filter :as status-filter]
            [report.routing :as routing]
            [report.components.app-content :refer [app-content-nicescroll app-content]]
            [report.components.app-bar :refer [app-bar]]
            [report.utils.log :refer [log log-o]]
            [jquery.main]
            [jquery.nicescroll]
            [report.routing :as routing])
  )



(def test-data (js->clj js/data :keywordize-keys true))

(log-o "td: " test-data)


(def path-category-map
  (let [f (fn [coll x]
            (let [{:keys [path category]} x]
              (update-in coll [category] #(conj % path))))]
    (reduce f {} test-data)))

(log-o "path-c-m" path-category-map)


(def test-data-structure
  (let [f (fn [coll x]
            (let [[k v] x
                  sub-tree (-> (structure/mk-tree v)
                               (structure/collapse-poor-branches))]
              (assoc coll k sub-tree)))]
    (reduce f {} path-category-map)))



(log-o "td-struct: " test-data-structure)



(def status-map (structure/mk-status-lists test-data #(get % :status) path/mk-path-combi))


(log-o "status map: " status-map)

(def status-filter-a (r/atom {}))

(status-filter/init-a-filter (keys (get status-map [])) status-filter-a)

(r/render-component [app (app-bar (fn [x] (condp = (count x)
                                            0 "SUCCESS"
                                            1 "SUCCESS"
                                            2 "FAIL"
                                            3 "UNDEF"
                                            4 "SUCCESS"
                                            "UNDEF")) routing/nav-position) (app-content test-data-structure status-map status-filter-a)]
                    (.getElementById js/document "app"))

