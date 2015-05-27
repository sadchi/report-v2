(ns report.components.table
  (:require [report.utils.log :refer [log log-o]]))


(defn- render-table-row [r-idx coll]
  ^{:key r-idx} [:tr.simple-table__tr (when (odd? r-idx) {:class "simple-table__tr--odd"})
                 (for [[idx value] (map-indexed vector coll)]
                   ^{:key idx} [:td.simple-table__td value])])



(defn table [{:keys [table-name columns-order data-a]}]
  (fn []
    (let [full-data @data-a
          ;_ (log-o "full-data" full-data)
          avail-column (set (keys full-data))
          ;_ (log-o "avail-column" avail-column)
          columns (filter #(contains? avail-column %) (map keyword columns-order))
          ;_ (log-o "columns" columns)
          selected-data (filter (complement nil?) (map #(get full-data %) columns))
          ;_ (log-o "selected-data" selected-data)
          transposed-data (partition (count avail-column) (apply interleave selected-data))
          ;_ (log-o "transposed-data" transposed-data)
          ]
      [:div.vertical-block
       [:h4 table-name]
       [:table.simple-table
        [:tr.simple-table__tr
         (for [[idx column] (map-indexed vector columns)
               :let [th (name column)]]
           ^{:key idx} [:th.simple-table__th th])]
        [:tbody (for [[idx data] (map-indexed vector transposed-data)]
                  (render-table-row idx data))]]])))