(ns report.test-results.structure
  (:require [report.utils.log :refer [log log-o]]))


(def leaf-content [:scenario])

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
  (if (= (get-in structure path) leaf-content)
    []
    (let [sub-tree (get-in structure path)
          childs (keys sub-tree)
          single-child? (= 1 (count childs))
          coll (if single-child?
                 [path (conj path (first childs))]
                 [])]
      (reduce into coll (map #(process-node structure (conj path %)) childs)))))

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
        pairs-dups-cleaned (partition 2 (reduce clear-dups [] pairs))
        ;operations should be reversed to start modifications from the bottom of the tree
        tailed-ops-reversed (reverse (map mk-tail pairs-dups-cleaned))
        new-structure (reduce apply-op structure tailed-ops-reversed)]
    new-structure))


(defn- add-level [v x]
  (conj v (conj (peek v) x)))

(defn- create-branch [path]
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


(defn is-node? [struct path]
  (map? (get-in struct path)))

(defn is-scenario? [struct path]
  (= leaf-content (get-in struct path)))

(defn is-run? [struct path]
  (if (empty? path)
    false
    (is-scenario? struct (pop path))))


(defn is-that-run? [target run]
  (let [run-target (get run :target)]
    (= target run-target)))

(defn transform-quarantine [q-coll]
  (let [f (fn [coll x]
            (update-in coll [(pop x)] #(conj % (peek x))))
        maped-q (reduce f {} q-coll)]
    maped-q))

(defn apply-quarantine [test-data-map quarantine]
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

(defn get-assets [run]
  (->> (get run :meta)
       (filter #(= "asset" (get % :type)))))