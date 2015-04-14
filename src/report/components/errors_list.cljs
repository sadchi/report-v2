(ns report.components.errors-list
  (:require [report.utils.string :refer [add-zero-spaces two-spaces->non-breaking]]
            [clojure.string :as string]
            [report.utils.log :refer [log log-o]]))


(defn- prepare-trace [trace-s]
  (let [
        ;_ (log-o "trace-s " trace-s)
        trace-s-lines (string/split trace-s #"\n")
        ;_ (log-o "trace-s-lines " trace-s-lines)
        trace-spanned (map (fn [x] [:span (add-zero-spaces (two-spaces->non-breaking x) 40)]) trace-s-lines)
        ;_ (log-o "trace-spanned " trace-spanned)
        trace-w-br (interpose [:br] trace-spanned)
        ;_ (log-o "trace-w-br " trace-w-br)
        trace-w-br-keyed (->> trace-w-br
                              (map-indexed vector)
                              (map #(with-meta (second %) {:key (first %)})))]
    trace-w-br-keyed))

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
                  error-trace (prepare-trace (get error :trace))]]
        ^{:key idx} [:tr.simple-table__tr {:class extra-class}
                     [:td.simple-table__td error-type]
                     [:td.simple-table__td
                      [:p error-message]
                      [:p error-trace]]])]]))

