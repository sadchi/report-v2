(ns report.components.badges
  (:require [report.test-results.statuses :refer [sort-statuses bad-status? good-status? neutral-status? get-worse get-best]]
            [report.test-results.curried-styles.status :refer [back-style]]))


(defn badged-text [status text small]
  (let [status-class (back-style status)
        small-class (when small "badge--small")
        class (str status-class " " small-class) ]
    [:div.badge {:class class} text]))

