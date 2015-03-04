(ns report.utils.log)


(defn log [& msgs]
  (.log js/console (apply str msgs)))

(defn log-o [s o]
  (.log js/console s (clj->js o)))

