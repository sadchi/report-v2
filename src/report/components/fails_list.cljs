(ns report.components.fails-list
  (:require [report.utils.string :refer [add-zero-spaces-near]]))


(defn fails-list [fails-coll]
  (when-not (empty? fails-coll)
    [:div.vertical-block
     [:h4 "Fails list:"]
     [:table.simple-table
      [:tr.simple-table__tr
       [:th.simple-table__th "Type"]
       [:th.simple-table__th "Message"]]
      (for [[idx fail] (map-indexed vector fails-coll)
            :let [extra-class (when (odd? idx) "simple-table__tr--odd")
                  fail-type (add-zero-spaces-near (get fail :type) ".")
                  fail-message (get fail :message)]]
        ^{:key idx} [:tr.simple-table__tr {:class extra-class}
                     [:td.simple-table__td fail-type]
                     [:td.simple-table__td fail-message]])]]))
