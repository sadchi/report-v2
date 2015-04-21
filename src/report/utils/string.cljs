(ns report.utils.string
  (:require [clojure.string :as string]
            [goog.string :as gstring]
            [goog.string.format]))

(defn add-zero-spaces [s every]
  (let [pattern (re-pattern (gstring/format  ".{1,%d}" every))]
    (string/join "\u200B" (re-seq pattern s))))

(defn add-zero-spaces-near [s target]
  (string/replace s target (str target "\u200B")))


(defn two-spaces->non-breaking [s]
  (string/replace s #"  " "\u00A0\u00A0"))