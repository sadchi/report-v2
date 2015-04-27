(ns report.test-results.statuses)

#_(def ^:private status-weight-map (clj->js ["FAIL" "ERROR" "WARNING" "UNDEFINED" "SKIPPED" "FAIL(q)" "ERROR(q)" "WARNING(q)" "UNDEFINED(q)" "SKIPPED(q)" "SUCCESS(q)" "SUCCESS"]))

#_(def ^:private status-vis-order-map (clj->js ["FAIL" "ERROR" "FAIL(q)" "ERROR(q)" "WARNING" "WARNING(q)" "UNDEFINED" "UNDEFINED(q)" "SKIPPED"   "SKIPPED(q)" "SUCCESS(q)" "SUCCESS"]))

(def ^:private status-specs {
                            :FAIL {:vis-idx 0
                                   :weight-idx 0
                                   :reputation :bad}
                            :ERROR {:vis-idx 1
                                    :weight-idx 1
                                    :reputation :bad}
                            :WARNING {:vis-idx 4
                                      :weight-idx 2
                                      :reputation :accent}
                            :UNDEFINED {:vis-idx 6
                                        :weight-idx 3
                                        :reputation :neutral}
                            :SKIPPED {:vis-idx 8
                                      :weight-idx 4
                                      :reputation :neutral}
                            :SUCCESS {:vis-idx 11
                                      :weight-idx 11
                                      :reputation :good}

                            :FAIL_Q {:vis-idx 2
                                     :weight-idx 5
                                     :reputation :semi-bad}
                            :ERROR_Q {:vis-idx 3
                                      :weight-idx 6
                                      :reputation :semi-bad}
                            :WARNING_Q {:vis-idx 5
                                        :weight-idx 7
                                        :reputation :semi-accent}
                            :UNDEFINED_Q {:vis-idx 7
                                          :weight-idx 8
                                          :reputation :neutral}
                            :SKIPPED_Q {:vis-idx 9
                                        :weight-idx 9
                                        :reputation :neutral}
                            :SUCCESS_Q {:vis-idx 10
                                        :weight-idx 10
                                        :reputation :semi-good}


                            })

(defn get-reputation [status]
  (let [status-keyword (keyword status)]
    (-> (get status-specs status-keyword)
        (get :reputation))))

(defn belong-to? [status coll]
  (let [rep (get-reputation status)
        expected-rep (set coll)]
    (contains? expected-rep rep)))

(defn- status-weight [status]
  (let [status-keyword (keyword status)]
    (-> (get status-specs status-keyword)
        (get :weight-idx))))

(defn- status-vis-idx [status]
  (let [status-keyword (keyword status)]
    (-> (get status-specs status-keyword)
        (get :vis-idx))))


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


(defn sort-statuses-by-weight [comp coll]
  (sort-by status-weight comp coll))

(defn sort-statuses-by-vis-order [comp coll]
  (sort-by status-vis-idx comp coll))


(defn get-worse [s-coll]
  (-> (sort-statuses-by-weight < s-coll)
      (first)))

(defn get-best [s-coll]
  (-> (sort-statuses-by-weight < s-coll)
      (last)))

(defn bad-status? [status]
  (belong-to? status [:bad]))

(defn good-status? [status]
  (belong-to? status [:good]))

(defn neutral-status? [status]
  (not (and (bad-status? status) (good-status? status))))

