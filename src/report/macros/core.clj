(ns report.macros.core
  (:require [clojure.string :as s]))

(defmacro get-css-desc [& classes]
  (let [
        gen-css-keyword (fn [x]
                          (keyword (str "." (name x))))
        names (map gen-css-keyword classes)
        combine (fn [x y]
                  (if (vector? y)
                    (into [x] y)
                    [x y]))]
    `(map #(if (vector? %2)(into [%1] %2) [%1 %2]) [~@names] [~@classes])))


(defmacro mk-fn [macro]
  `(fn [& args#] (eval (cons '~macro args#))))



(defmacro get-css-class-names [& classes]
  (s/join " " (map name classes)))


(defmacro class-name [class]
  (name class))

(defmacro classes [& args]
  {:class (apply (mk-fn get-css-class-names) args)})

