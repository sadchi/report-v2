(ns report.test-results.curried-styles.status
  (:require [report.test-results.statuses :refer [good-status? bad-status?]]))


#_(defn class-selector [status class-names-coll]
  (let [status-name (name status)]
    (cond
      (good-status? status-name) (get class-names-coll :good)
      (bad-status? status-name) (get class-names-coll :bad)
      :else (get class-names-coll :neutral))))

(defn back-style [reputation]
  (get {:good        "success-back-theme"
        :semi-good   "semi-success-back-theme"
        :bad         "error-back-theme"
        :semi-bad    "semi-error-back-theme"
        :accent      "accent-back-theme"
        :semi-accent "semi-accent-back-theme"
        :neutral     "neutral-back-theme"} reputation))

#_(defn text-style [status]
  (class-selector status {:good    "success-text-theme"
                          :bad     "error-text-theme"
                          :neutral "neutral-text-theme"}))

