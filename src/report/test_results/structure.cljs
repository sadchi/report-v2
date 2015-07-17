(ns report.test-results.structure
  (:require [report.utils.log :refer [log log-o]]))


(def leaf-content :scenario)

(defn build-map-by [coll key-field]
  (let [f (fn [coll x]
            (assoc coll (get x key-field) x))]
    (reduce f {} coll)))

(defn mk-tree [paths]
  (reduce #(assoc-in %1 %2 leaf-content) {} paths))

(defn dissoc-in
  "Dissociates an entry from a nested associative structure returning a new
  nested structure. keys is a sequence of keys. Any empty maps that result
  will not be present in the new structure."
  [m [k & ks :as keys]]
  (if ks
    (if-let [nextmap (get m k)]
      (let [newmap (dissoc-in nextmap ks)]
        (if (seq newmap)
          (assoc m k newmap)
          (dissoc m k)))
      m)
    (dissoc m k)))

(defn- process-node [structure path]
  (let [sub-tree (get-in structure path)]
    (if-not (map? sub-tree)
     []
     (let [childs (keys sub-tree)
           single-child? (= 1 (count childs))
           coll (if single-child?
                  [path (conj path (first childs))]
                  [])]
       (reduce into coll (map #(process-node structure (conj path %)) childs))))))

(defn- get-single-child-pairs [structure]
  (reduce into []
          (for [k (keys structure)]
            (process-node structure [k]))))

(defn collapse-poor-branches [structure]
  (let [clear-dups (fn [coll x]
                     (let [prev (peek coll)]
                       (cond
                         (= prev nil) [x]
                         (= prev x) (pop coll)
                         :else (conj coll x))))

        path-tail (fn [x y]
                    (subvec y (dec (count x))))

        mk-tail (fn [x]
                  (let [[x1 x2] x]
                    [x1 x2 (path-tail x1 x2)]))


        apply-op (fn [coll x]
                   (let [[dst-path src-path tail] x
                         new-path (pop dst-path)
                         val (get-in coll src-path)]
                     (-> coll
                         (dissoc-in dst-path)
                         (assoc-in (conj new-path tail) val))))


        pairs (get-single-child-pairs structure)
        ;_ (log-o "pairs" pairs)
        pairs-dups-cleaned (partition 2 (reduce clear-dups [] pairs))
        ;_ (log-o "pairs-dups-cleaned" pairs-dups-cleaned)
        ;operations should be reversed to start modifications from the bottom of the tree
        tailed-ops-reversed (reverse (map mk-tail pairs-dups-cleaned))
        new-structure (reduce apply-op structure tailed-ops-reversed)]
    new-structure))


(defn- add-level [v x]
  (conj v (conj (peek v) x)))

(defn create-branch [path]
  (reduce add-level [[]] path))


(defn- add-status-list [x y]
  (let [f (fn [coll a]
            (let [[k v] a
                  old-val (get coll k 0)]
              (assoc coll k (+ old-val v))))]
    (reduce f x y)))




(defn mk-status-lists [test-data get-statuses-map-fn mk-path-fn]
  (let [path-status-map (map (fn [x]
                               (let [path (mk-path-fn x)
                                     status (get-statuses-map-fn x)]
                                 [path status])) test-data)

        ;_ (log-o "path-status-map" path-status-map)
        f-add-status (fn [status-map coll x]
                       (let [old-val (get coll x)
                             new-val (add-status-list old-val status-map)]
                         (assoc coll x new-val)))

        f (fn [coll x]
            (let [[path statuses] x
                  branch (create-branch path)]
              (reduce (partial f-add-status statuses) coll branch)))]
    (reduce f {} path-status-map)))



(defn- add-2-m [m1 m2]
  (let [f (fn [coll [k v]]
            (assoc coll k (+ v (get coll k 0))))]
    (reduce f m1 m2)))

(defn- add-summary [new old]
  (if (nil? old)
    new
    (let [{a-errors :errors a-fails :fails a-badges :badges } old
          {b-errors :errors b-fails :fails b-badges :badges } new
          badges (add-2-m a-badges b-badges)
          errors (add-2-m a-errors b-errors)
          fails (add-2-m a-fails b-fails)]
      {:badges badges
       :errors errors
       :fails fails})))

(defn- mk-tree-map [coll get-attr-f add-attr-f mk-path-f]
  (let [path-n-attrs (map (fn [x] [(mk-path-f x) (get-attr-f x)]) coll)

        apply-attr (fn [new-attr coll path]
                     (update coll path add-attr-f new-attr))

        f (fn [coll [path attr]]
            (let [branch (create-branch path)]
              (reduce (partial apply-attr attr) coll branch)))]
    (reduce f {} path-n-attrs)))


(defn mk-summary-map [coll get-attr-f  mk-path-f]
  (mk-tree-map coll get-attr-f  add-summary mk-path-f))

(defn is-node? [struct path]
  (map? (get-in struct path)))

(defn is-scenario? [struct path]
  (= leaf-content (get-in struct path)))

(defn is-run? [test-data-map path]
  ;(log-o "path is run" path)
  (if (empty? path)
    false
    (let [test-path (-> path
                        pop
                        flatten
                        rest)
          ;_ (log-o "test-path" test-path)
          res (not (nil? (get test-data-map test-path)))
          ;_ (log-o "res" res)
          ]
      res)))


(defn is-that-run? [target run]
  (let [run-target (get run :target)]
    (= target run-target)))

(defn transform-quarantine [q-coll]
  (let [f (fn [coll x]
            (update-in coll [(pop x)] #(conj % (peek x))))
        maped-q (reduce f {} q-coll)]
    maped-q))

(defn apply-quarantine-old [test-data-map quarantine]
  (let [mk-status-map (fn [coll x]
                        (let [status (keyword (get x :status))
                              status-count (get coll status 0)]
                          (assoc coll status (inc status-count))))

        quarantine-runs (fn [target-set coll x]
                          (let [
                                ;_ (log-o "target-set " target-set)
                                target (get x :target)
                                ;_ (log-o "target " target)
                                status (get x :status)
                                ;_ (log-o "status " status)
                                quarantine? (contains? target-set target)
                                ;_ (log-o "quarantine? " quarantine?)
                                ]
                            (if-not quarantine?
                              (conj coll x)
                              (conj coll (assoc x :status (str status "_Q"))))))
        f (fn [coll x]
            (let [
                  ;_ (log-o "x: " x)
                  [path target-set] x
                  ;_ (log-o "path: " path)
                  ;_ (log-o "target-set: " target-set)
                  scen-info (get coll path)
                  runs (get scen-info :runs)
                  ;_ (log-o "runs: " runs)
                  runs-w-quarantine (reduce (partial quarantine-runs (set target-set)) [] runs)
                  ;_ (log-o "runs q: " runs-w-quarantine)
                  status-map (reduce mk-status-map {} runs-w-quarantine)
                  ;_ (log-o "status map: " status-map)
                  new-scen-info (-> (assoc scen-info :runs runs-w-quarantine)
                                    (assoc :status status-map))]
              (assoc coll path new-scen-info)))]
    (reduce f test-data-map quarantine)))


(defn apply-quarantine [test-data-map id->path quarantine]
  (let [apply-f->map-item (fn [m k f]
                            (let [key (keyword k)
                                  old-v (get m key 0)
                                  new-v (f old-v)
                                  ;_ (log-o "m " m)
                                  ;_ (log-o "k " key)
                                  ;_ (log-o "old-v " old-v)
                                  ;_ (log-o "new-v " new-v)
                                  ]
                              (cond
                                (pos? new-v) (assoc m key new-v)
                                :else (dissoc m key))))
        apply-q (fn [coll x]
                  (let [q-status (str x "_Q")]
                    (-> coll
                        (apply-f->map-item x dec)
                        (apply-f->map-item q-status inc))))
        f (fn [coll x]
            (let [[id q-list] x
                  path (get id->path id)
                  scenario (get test-data-map path)
                  status-map (get scenario :status)
                  new-status-map (reduce apply-q status-map (vals q-list))
                  ]
              (assoc-in coll [path :status] new-status-map)))]
    (reduce f test-data-map quarantine)))


(defn get-assets [run]
  (->> (get run :meta)
       (filter #(= "asset" (get % :type)))))

(defn get-runs-count [runs]
  (count (.keys js/Object runs)))


(defn build-id->path [test-data]
  (let [f (fn [coll x]
            (let [{:keys [id path]} x]
              (assoc coll id path)))]
    (reduce f {} test-data)))


(defn- find-leaf [structure path]
  (if (= (get-in structure path) leaf-content)
    [path]
    (let [sub-tree (get-in structure path)
          childs (keys sub-tree)]
      (reduce into [] (map #(find-leaf structure (conj path %)) childs)))))


(defn- tree->path-list [structure]
  (if-not (map? structure)
    nil
    (reduce into []
            (for [k (keys structure)]
              (find-leaf structure [k])))))
