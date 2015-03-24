(ns report.test-results.path
  (:require [clojure.string :refer [join]]))


(defn mk-path-combi [x]
  (let [cat (get x :category)
        path (get x :path)]
    (into [cat] path)))

(defn flatten-path [p]
  (if (vector? p)
    (into [] (flatten p))
    [p]))

(defn path->str [p]
  (if (vector? p)
    (join " / " p)
    p))
