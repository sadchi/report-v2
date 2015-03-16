(ns report.test-results.statuses)

(def ^:private status-weight-map (clj->js ["FAIL" "ERROR" "UNDEFINED" "SKIPPED" "SUCCESS"]))

(def ^:private bad-statuses #{"FAIL" "ERROR"})

(def ^:private good-statuses #{"SUCCESS"})

(def ^:private common-statuses #{"FAIL" "SUCCESS"})

(defn evaluate-status [status]
  (cond
    (good-statuses status) :good
    (bad-statuses status) :bad
    :else :neutral))
