(ns report.test-results.path
  (:require [clojure.string :as s :refer [join escape]]))


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


(defn replace-several [content & replacements]
  (let [replacement-list (partition 2 replacements)]
    (reduce #(apply s/replace %1 %2) content replacement-list)))

(defn safe-path [x]
  (s/replace x #"/" "*"))

(defn desafe-path [x]
  (s/replace x #"\*" "/"))