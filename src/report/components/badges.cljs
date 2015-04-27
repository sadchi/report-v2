(ns report.components.badges
  (:require [report.test-results.statuses :refer [sort-statuses-by-weight bad-status? good-status? neutral-status? get-worse get-best]]
            [report.test-results.curried-styles.status :refer [back-style]]))


(defn badged-text [reputation text small]
  (let [reputation-class (back-style reputation)
        small-class (when small "badge--small")
        class (str reputation-class " " small-class) ]
    [:div.badge {:class class} text]))

