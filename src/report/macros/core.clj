(ns report.macros.core
  (:require [clojure.string :as s]))

(defmacro mk-fn [macro]
  `(fn [& args#] (eval (cons '~macro args#))))

(defmacro get-css-class-names [& classes]
  (s/join " " (map name classes)))

(defmacro class-name [class]
  (name class))

(defmacro classes [& args]
  {:class (apply (mk-fn get-css-class-names) args)})

