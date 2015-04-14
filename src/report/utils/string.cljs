(ns report.utils.string
  (:require [clojure.string :as string]
            [goog.string :as gstring]
            [goog.string.format]))

(defn add-zero-spaces [str every]
  (let [pattern (re-pattern (gstring/format  ".{1,%d}" every))]
    (string/join "\u200B" (re-seq pattern str))))


(defn two-spaces->non-breaking [str]
  (string/replace str #"  " "\u00A0\u00A0"))