(ns report.components.runs-meta
  (:require [report.components.pivot-table :refer [pivot-table-factory]]))


(defn meta-text [meta-item]
  [:div.vertical-block (:data meta-item)])

(defn is-table-empty? [coll]
  (every? nil? coll))


(defn render-table-row [r-idx coll]
  ^{:key r-idx} [:tr.simple-table__tr (when (odd? r-idx) {:class "simple-table__tr--odd"})
                 (for [[idx value] (map-indexed vector coll)]
                   ^{:key idx} [:td.simple-table__td value])])

(defn meta-table [meta-item]
  [:div.vertical-block
   [:h4 (:name meta-item)]
   [:table.simple-table
    [:tr.simple-table__tr
     (for [[idx th] (map-indexed vector (:columns meta-item []))]
       ^{:key idx} [:th.simple-table__th th])]
    (loop [data (:data meta-item)
           idx 0
           acc nil]
      (if (is-table-empty? data)
        (reverse acc)
        (->> data
             (map first)
             (render-table-row idx)
             (conj acc)
             (recur (map next data) (inc idx)))))]])

(defn meta-pivot-table [meta-item]
  (let [[selected-data-a & dropdowns] (pivot-table-factory meta-item)]
    [:div.vertical-block
    [:h4 (:name meta-item)]
     (for [[idx dropdown] (map-indexed vector dropdowns)
           :let [[dropdown-name component] dropdown]]
       ^{:key idx} [:div dropdown-name [component]])
    ]))



(defn meta-data-render [meta-data]
  [:div
   [:h3 "Meta information:"]
   (for [[idx meta-item] (map-indexed vector meta-data)]
     (case (:type meta-item)
       "text" ^{:key idx} [meta-text meta-item]
       "table" ^{:key idx} [meta-table meta-item]
       "pivot-table" ^{:key idx} [meta-pivot-table meta-item]
       nil))])