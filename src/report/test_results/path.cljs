(ns report.test-results.path)


(defn mk-path-combi [x]
  (let [cat (get x :category)
        path (get x :path)]
    (into [cat] path)))
