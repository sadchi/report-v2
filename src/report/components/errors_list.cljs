(ns report.components.errors-list
  (:require [report.utils.string :refer [add-zero-spaces]]))


(defn errors-list [errors-coll]
  (when-not (empty? errors-coll)
    [:div.vertical-block
     [:h4 "Exceptions list:"]
     [:table.simple-table
      [:tr.simple-table__tr
       [:th.simple-table__th "Type"]
       [:th.simple-table__th "Message/Trace"]]
      (for [[idx error] (map-indexed vector errors-coll)
            :let [extra-class (when (odd? idx) "simple-table__tr--odd")
                  error-type (get error :type)
                  error-message (get error :message)
                  error-trace (get error :trace)]]
        ^{:key idx} [:tr.simple-table__tr {:class extra-class}
                     [:td.simple-table__td error-type]
                     [:td.simple-table__td
                      [:p error-message]
                      [:p (add-zero-spaces error-trace 10)]]])]]))

