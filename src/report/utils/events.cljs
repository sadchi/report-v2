(ns report.utils.events
  (:require [report.utils.log :refer [log log-o]]))


(defn debounce
  ([f] (debounce f 1000))
  ([f timeout]
   (let [id (atom nil)]
     (fn []
       (if (not (nil? @id))
         (js/clearTimeout @id))
       (reset! id (js/setTimeout
                    (fn []
                      ;(log "bingo")
                      (f))
                    timeout))))))