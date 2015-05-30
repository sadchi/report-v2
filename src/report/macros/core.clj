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
  (s/join " " (map name classes)))

(defmacro mk-fn [macro]
  `(fn [& args#] (eval (cons '~macro args#))))

(defmacro classes [& args]
  {:class (apply (mk-fn get-css-class-names) args)})