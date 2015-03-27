(ns report.components.badges
  (:require [report.test-results.statuses :refer [sort-statuses bad-status? good-status? neutral-status? get-worse get-best]]))


(defn badged-count [status cnt small]
  (let [named-status (name status)
        status-class (cond
                       (good-status? named-status) "success-back-theme"
                       (bad-status? named-status) "error-back-theme"
                       :else "neutral-back-theme")
        small-class (when small "badge--small")
        class (str status-class " " small-class) ]
    (fn []
      [:div.badge {:class class} cnt])))

