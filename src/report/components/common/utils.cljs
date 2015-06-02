(ns report.components.common.utils
  (:require [garden.color :as color]
            [garden.core :refer [css]]
            [clojure.string :as s]))



(defn ^:private node [tag content]
  (let [elem (.createElement js/document tag)]
    (set! (.-innerHTML elem) content)
    elem))

(defn add-style! [css]
  (.appendChild (.-head js/document) (node "style" css)))


(defn rgba [hex-color alpha]
  (let [rgb-map (color/hex->rgb hex-color)
        {r :red g :green b :blue} rgb-map]
    (str "rgba(" r "," g "," b "," alpha ")")))


(defn mk-garden-desc [x]
  "pairs [sym-name sym-var] got from the ns-interns"
  (let [[sym-name sym-var] x
        class-keyword (keyword (str "." (name sym-name)))
        class-val @sym-var]
    (if (vector? class-val)
      (into [class-keyword] class-val)
      [class-keyword class-val])))

(defn class-names [& cls]
  (s/join " " (map name cls)))

(defn classes [& cls]
  {:class (apply class-names cls)})

(defn attr [m]
  (let [classes-v (get m :classes)
        class (if (or (vector? classes-v) (seq? classes-v))
                (apply class-names classes-v)
                (class-names classes-v))]
    (assoc m :class class)))

(def css-w-prefixes
  (partial css {:vendors     ["webkit" "mos" "ms"]
                :auto-prefix #{:flex :flex-grow :flex-shrink :flex-direction :align-content :align-self :justify-content :flex-basis}}))