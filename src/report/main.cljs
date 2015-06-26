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
            [report.components.tool-bar :as t :refer [tool-bar]]
            [report.components.tooltip :refer [tooltip]]
            [report.utils.log :refer [log log-o]]
            [jquery.main]
            [jquery.nicescroll]
            [report.routing :as routing]
            [report.components.dropdown])
  )



#_(.profile js/console "main" )

(extend-protocol ILookup
  object
  (-lookup [m k] (let [key (name k)] (aget m key)))
  (-lookup [m k not-found] (let [key (name k)] (or (aget m key) not-found))))



(def test-data (js->clj js/data :keywordize-keys true))
(def runs js/runs)


(log-o "td: " test-data)


(def id->path (structure/build-id->path test-data))
(log-o "id->path: " id->path)


(def test-data-map (structure/build-map-by test-data :path))





(log-o "td map: " test-data-map)


(def quarantine
  (try
    (js->clj js/quarantine)
    (catch js/Error _ [])))
(log-o "quarantine " quarantine)
;
;
;
;
(def test-data-quarantined
  (structure/apply-quarantine test-data-map id->path quarantine))


(log-o "test-data-quarantine " test-data-quarantined)


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




(def status-map (structure/mk-status-lists (vals test-data-quarantined) #(get % :status) path/mk-path-combi))



(log-o "status map: " status-map)

(def status-filter-a (r/atom {}))

(status-filter/init-a-filter (keys (get status-map [])) status-filter-a)



(defn- get-status [{:keys [test-data-map runs quarantine struct status-map path]}]
  (let [run? (structure/is-run? struct path)
        ;_ (log-o "run? " run?)
        flat-path (path/flatten-path path)
        ;_ (log-o "path " path)
        ;_ (log-o "flat-path " flat-path)
        ]
    (if run?
      (let [
            ;_ (log-o "test-data-map " test-data-map)
            scenario-info (get test-data-map (rest (pop flat-path)))
            ;_ (log-o "scen info " scenario-info)
            scen-id (get scenario-info :id)

            ;_ (log-o "runs " runs)
            target (peek path)
            scen-runs (get runs scen-id)
            scen-quarantine (get quarantine scen-id)
            before-q-status (get scen-quarantine target)
            run (get scen-runs target)
            status (if before-q-status
                     (str before-q-status "_Q")
                     (get run :status))
            ]
        status)
      (let [unit-status-map (get status-map flat-path)
            ;_ (log-o "unit-status-map " unit-status-map)
            statuses (map name (keys unit-status-map))
            ;_ (log-o "statuses " statuses)
            worse-status (statuses/get-worse statuses)
            ;_ (log-o "worse " worse-status)
            best-status (statuses/get-best statuses)
            ;_ (log-o "best " worse-status)
            res-status (cond
                         (statuses/bad-status? worse-status) worse-status
                         (and
                           (statuses/neutral-status? worse-status)
                           (statuses/good-status? best-status)) best-status
                         :else worse-status)
            ;_ (log-o "res " worse-status)
            ]
        (name res-status)))))

#_(.profileEnd js/console)

(defn- target-slice? [a-nav-pos]
  (let [nav-pos @a-nav-pos
        nav-pos-meta (meta nav-pos)
        slice (get nav-pos-meta :slice)
        active? (or (not slice) (= slice "target"))]
    active?))

(defn- fail-type-slice? [a-nav-pos]
  (let [nav-pos @a-nav-pos
        nav-pos-meta (meta nav-pos)
        slice (get nav-pos-meta :slice)
        active? (= slice "failtype")]
    active?))

(defn switch->fail-type-href [a-nav-pos]
  (let [nav-pos @a-nav-pos
        uri (routing/path->uri nav-pos)]
    (str uri "?slice=failtype")))

(r/render-component [app (app-bar #(get-status {:test-data-map test-data-quarantined
                                                :quarantine    quarantine
                                                :runs          runs
                                                :struct        test-data-structure
                                                :status-map    status-map
                                                :path          %}) routing/nav-position)
                     (app-content {:test-data-map   test-data-quarantined
                                   :quarantine      quarantine
                                   :runs            runs
                                   :struct          test-data-structure
                                   :status-map      status-map
                                   :status-filter-a status-filter-a
                                   :nav-position-a  routing/nav-position})
                     (t/tool-bar (t/tool-bar-label "Slice:")
                                 (t/tool-bar-action-label {:text "Target"
                                                           :is-active-f? #(target-slice? routing/nav-position)
                                                           :get-href-f #(routing/path->uri @routing/nav-position)})
                                 (t/tool-bar-action-label {:text "FailType"
                                                           :is-active-f? #(fail-type-slice? routing/nav-position)
                                                           :get-href-f #(switch->fail-type-href routing/nav-position)}))]
                    (.getElementById js/document "app"))

(r/render-component [tooltip] (.getElementById js/document "tooltip"))
