(ns report.components.assets-list
  (:require [report.utils.string :refer [add-zero-spaces]]))


(defn assets-list [assets-coll]
  (when-not (empty? assets-coll)
    [:div.vertical-block
     [:h4 "Assets:"]
     [:table.simple-table
      [:tr.simple-table__tr
       [:th.simple-table__th "Name"]
       [:th.simple-table__th "Link"]]
      (for [[idx asset] (map-indexed vector assets-coll)
            :let [extra-class (when (odd? idx) "simple-table__tr--odd")
                  asset-name (get asset :name)
                  asset-link (get asset :value)]]
        ^{:key idx} [:tr.simple-table__tr {:class extra-class}
                     [:td.simple-table__td asset-name]
                     [:td.simple-table__td (add-zero-spaces asset-link 10)]])]]))
