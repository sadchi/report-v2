(ns report.components.badges
  (:require [report.test-results.statuses :refer [sort-statuses bad-status? good-status? neutral-status? get-worse get-best]]))


(defn badged-text [status text small]
  (let [named-status (name status)
        status-class (cond
                       (good-status? named-status) "success-back-theme"
                       (bad-status? named-status) "error-back-theme"
                       :else "neutral-back-theme")
        small-class (when small "badge--small")
        class (str status-class " " small-class) ]
    [:div.badge {:class class} text]))

