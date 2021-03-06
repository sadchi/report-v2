(ns report.components.pivot-table
  (:require [report.components.dropdown :refer [dropdown]]
            [report.components.common.utils :as u]
            [report.components.styles.core :as s]
            [report.components.styles.params :as p]
            [report.components.table :refer [table]]
            [reagent.core :as r]
            [report.utils.log :refer [log log-o]]
            [clojure.set :as set]
            [garden.units :refer [px]]))


(defn- compose [columns data])

(defn filter-data [filters data-map]
  (let [get-bad-indices (fn [res x]
                          (let [[k v] x
                                any? (= "any" (str v))
                                ;_ (log-o "k " k)
                                ;_ (log-o "v " v)
                                ;_ (log-o "any? " any?)
                                ]
                            (if any?
                              res
                              (loop [idx 0
                                     r-coll (get data-map k)
                                     acc res]
                                (let [
                                      ;_ (log-o "idx " idx)
                                      ;_ (log-o "r-coll " r-coll)
                                      ;_ (log-o "acc " acc)
                                      current-v (first r-coll)
                                      new-acc (if (= current-v v)
                                                acc
                                                (conj acc idx))
                                      new-r-coll (rest r-coll)]
                                  (if (empty? new-r-coll)
                                    new-acc
                                    (recur (inc idx) new-r-coll new-acc)))))))

        extract-good-data (fn [good-idx exclude-keys coll x]
                            (let [[k v] x
                                  ;_ (log-o "k" k)
                                  ;_ (log-o "v" v)
                                  ;_ (log-o "ex" exclude-keys)
                                  ;_ (log-o "gi" good-idx)
                                  ;_ (log-o "contains? " (contains? exclude-keys k))
                                  ]
                              (if (contains? exclude-keys k)
                                coll
                                (let [with-idx (map-indexed vector v)
                                      filter-out (fn [coll x]
                                                   (let [[idx v] x]
                                                     (if (contains? good-idx idx)
                                                       (conj coll v)
                                                       coll)))
                                      extracted (reduce filter-out [] with-idx)]
                                  (assoc coll k extracted)))))


        bad-indices (reduce get-bad-indices #{} filters)
        ;_ (log-o "bad-indices " bad-indices)
        [_ column] (first data-map)
        indices (set (range (count column)))
        ;_ (log-o "indices " indices)
        good-indices (set/difference indices bad-indices)
        ;_ (log-o "good-indices " good-indices)
        res-data (reduce (partial extract-good-data good-indices (set (keys filters))) {} data-map)
        ;_ (log-o "res-data " res-data)
        ]
    res-data))


(defn pivot-table-factory [{:keys [pivots columns data]}]
  "Returns collection of components. The pivot dropdown-list in the beginning and table in the end.
  pivot-coll should be like [ {:name column-1 :traits [] :current
  } column-2 column-3 [:non-strict column-4]]"
  (let [add-coll (fn [data-map coll x]
                   (let [[k v] x
                         ;_ (log-o "k " k)
                         column (get data-map k)
                         ;_ (log-o "column " column)
                         unique-values (sort (set column))
                         new-v (assoc v :coll unique-values)]
                     (assoc coll k new-v)))

        mk-initial-filters (fn [coll x]
                             (let [[k v] x
                                   current (get v :current)]
                               (assoc coll k current)))


        columns-keyworded (map keyword columns)
        data-map (zipmap columns-keyworded data)
        ;_ (log-o "data-map " data-map)

        filters (atom (reduce mk-initial-filters {} pivots))
        ;_ (log-o "filters " @filters)

        selected-data-a (r/atom (filter-data @filters data-map))
        ;_ (log-o "selected-data-a " @selected-data-a)

        pivots-w-coll (reduce (partial add-coll data-map) {} pivots)
        ;_ (log-o "pivots-w-coll " pivots-w-coll)


        dropdowns (for [item pivots-w-coll
                        :let [[k v] item
                              ;_ (log-o "k" k)
                              ;_ (log-o "coll" (get v :coll))
                              ;_ (log-o "any?" (get v :any))
                              ;_ (log-o "current" (get v :current))
                              ]]
                    [(name k) (dropdown {:coll                              (get v :coll)
                                         :any?                              (get v :any)
                                         :current                           (get v :current)
                                         :width                             :m
                                         :select-fn #_(log-o "selected " %) (fn [x]
                                                                              (swap! filters assoc k x)
                                                                              ;(log-o "current filter" @filters)
                                                                              (reset! selected-data-a (filter-data @filters data-map)))})])
        ]
    [selected-data-a dropdowns]))


(defn pivot-simple-table [meta-item]
  (let [[selected-data-a dropdowns] (pivot-table-factory meta-item)
        ;_ (log-o "dropdowns" dropdowns)
        ]
    (fn []
      [:div.vertical-block
       [:h4 (:name meta-item)]
       [:div.hor-sub-block-m
        [:p "Filter data in the table by the next filters:"]
        [:div.pivot-table-filters
         [:div.pivot-table-filters__filter-name-column
          (for [[idx f-name] (map-indexed vector (map first dropdowns))]
            ^{:key idx} [:span.pivot-table-filters__row (str f-name ": ")])]
         [:div.pivot-table-filters__filters-column
          (for [[idx component] (map-indexed vector (map second dropdowns))]
            ^{:key idx} [:div.hor-sub-block-s.pivot-table-filters__row [component]])]]
        [table {:columns-order (get meta-item :columns)
                :data-a        selected-data-a}]]])))



(defonce ^:private styles
  [
   [:.pivot-table-filters__row
    {:height (px p/line-height)
     :line-height (px p/line-height)}]
   [:.pivot-table-filters
    s/flex-box]
   [:.pivot-table-filters__filter-name-column
    s/flex-box
    {:flex-grow 0
     :flex-direction "column"
     :align-items "flex-end"}]
   [:.pivot-table-filters__filters-column
    s/flex-box
    {:flex-grow 1
     :flex-direction "column"}]
   [:.pivot-table-filters__item
    s/hor-sub-block]
   ])


(defonce init
  (let [name (namespace ::x)]
    (u/add-style! (u/css-w-prefixes {:pretty-print? true} styles))
    (log (str name " ... initialized"))))