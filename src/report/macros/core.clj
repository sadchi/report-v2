(ns report.macros.core
  (:require [clojure.string :as s]))

(defmacro def-css-class [class-sym declaration]
  `(def ~class-sym (with-meta [(keyword (str "." (quote ~class-sym))) ~declaration] {:name (str (quote ~class-sym))})))

(defmacro get-css-desc [& classes]
  (let [gen-css-name (fn [x]
                       (str ":." (name x)))
        names (map gen-css-name classes)
        res# (map vector names classes)]
    `[~@res#]))

(defmacro get-css-class-names [& classes]
  `(str ~@(s/join " " (map name classes))))

(defmacro classes [& args]
  `(assoc {} :class ~@(s/join " " (map name args))))