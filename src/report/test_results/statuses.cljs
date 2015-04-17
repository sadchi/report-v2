(ns report.test-results.statuses)

(def ^:private status-weight-map (clj->js ["FAIL" "ERROR" "WARNING" "UNDEFINED" "SKIPPED" "FAIL(q)" "ERROR(q)" "WARNING(q)" "UNDEFINED(q)" "SKIPPED(q)" "SUCCESS(q)" "SUCCESS"]))

(def ^:private bad-statuses #{"FAIL" "ERROR"})

(def ^:private good-statuses #{"SUCCESS"})

(def ^:private common-statuses #{"FAIL" "SUCCESS"})


(defn evaluate-status [status]
  (let [status-name (name status)]
    (cond
     (good-statuses status-name) :good
     (bad-statuses status-name) :bad
     :else :neutral)))

(defn- status-weight [x]
  (let [status-name (name x)]
    (.indexOf status-weight-map status-name)))

(defn- compare-status [fn s1 s2]
  (let [s1-w (status-weight s1)
        s2-w (status-weight s2)]
    (if (fn s1-w s2-w)
      s1
      s2)))

(defn better-status [s1 s2]
  (compare-status > s1 s2))

(defn worse-status [s1 s2]
  (compare-status < s1 s2))

(defn sort-statuses [comp coll]
  (sort-by status-weight comp coll))

(defn get-worse [s-coll]
  (-> (sort-statuses < s-coll)
      (first)))

(defn get-best [s-coll]
  (-> (sort-statuses < s-coll)
      (last)))

(defn bad-status? [s]
  (contains? bad-statuses (name s)))

(defn good-status? [s]
  (contains? good-statuses (name s)))

(defn neutral-status? [s]
  (not (and (bad-status? s) (good-status? s))))

(defn common-status? [s]
  (contains? common-statuses (name s)))