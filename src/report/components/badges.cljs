(ns report.components.badges
  (:require [report.utils.log :refer [log log-o]]
    [report.test-results.statuses :refer [sort-statuses-by-weight bad-status? good-status? neutral-status? get-worse get-best]]
            [report.components.common.utils :as u]
            [report.components.styles.color-themes :as t]
            [report.components.styles.badges :as b]))


(defn badged-text [reputation text small]
  (let [reputation-class (t/back-style-by-reputation reputation)
        classes (list 'b/neu-badge reputation-class)
        final-classes (if small
                        (conj classes 'b/neu-badge--small)
                        classes)]
    [:div (u/attr {:classes final-classes}) text]))

