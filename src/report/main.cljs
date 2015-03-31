(ns report.main
  (:require [reagent.core :as r]
            [report.test-results.structure :as structure]
            [report.test-results.path :as path]
            [report.test-results.statuses :as statuses]
            [report.components.app :refer [app]]
            [report.components.status-filter :as status-filter]
            [report.routing :as routing]
            [report.components.app-content :refer [app-content]]
            [report.components.app-bar :refer [app-bar]]
            [report.utils.log :refer [log log-o]]
            [jquery.main]
            [jquery.nicescroll]
            [report.routing :as routing])
  )



(def test-data (js->clj js/data :keywordize-keys true))

(log-o "td: " test-data)

(def test-data-map (structure/build-map-by test-data :path))

(log-o "td map: " test-data-map)

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


(defn- get-status [status-map path]
  (let [
        ;_ (log-o "path " path)
        flat-path (path/flatten-path path)
        ;_ (log-o "flat path " flat-path)
        unit-status-map (get status-map flat-path)
        statuses (map name (keys unit-status-map))
        ;_ (log-o "status keys " statuses)
        worse-status (statuses/get-worse statuses)
        ;_ (log-o "worse status " worse-status)

        best-status (statuses/get-best statuses)
        ;_ (log-o "best status " best-status)
        ;_ (log (statuses/good-status? best-status))
        res-status (cond
                     (statuses/bad-status? worse-status) worse-status
                     (and
                       (statuses/neutral-status? worse-status)
                       (statuses/good-status? best-status)) best-status
                     :else worse-status)
        ;_ (log-o "res status " res-status)
        ]
    (name res-status)))

(r/render-component [app (app-bar (partial get-status status-map) routing/nav-position)
                     (app-content {:test-data test-data-map
                                   :struct test-data-structure
                                   :status-map status-map
                                   :status-filter-a status-filter-a
                                   :nav-position-a routing/nav-position})]
                    (.getElementById js/document "app"))

