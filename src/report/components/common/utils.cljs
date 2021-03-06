(ns report.components.common.utils
  (:require [garden.color :as color]
            [garden.core :refer [css]]
            [clojure.string :as s]
            [report.utils.log :refer [log log-o]]))


(defn ^:private node [tag content ns]
  (let [elem (.createElement js/document tag)]
    (set! (.-innerHTML elem) content)
    (.setAttribute elem "ns" ns)
    elem))

(defn add-style! [css & {ns :ns}]
  (.appendChild (.-head js/document) (node "style" css ns)))


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

(defn is-css? [[_ x]]
  (get (meta @x) :css))

(defn mk-ns-classes [interns]
  (let [css-vars (filter is-css? (sort-by first interns))
        ;_ (log-o "css vars " css-vars)
        css-structs (map mk-garden-desc css-vars)
        ;_ (log-o "css vars " (str css-structs))
        ]
    css-structs))

(defn class-names [& cls]
  (s/join " " (map name cls)))

(defn classes [& cls]
  {:class (apply class-names cls)})

(defn attr [m]
  (let [classes-v (get m :classes)
        class (if (or (vector? classes-v) (seq? classes-v))
                (apply class-names (filter some? classes-v))
                (class-names classes-v))]
    (assoc m :class class)))

(defn at [& {classes :classes :as full-map}]
  (assoc full-map :class (if (or (vector? classes) (seq? classes))
                           (apply class-names (filter some? classes))
                           (class-names classes))))


(def css-w-prefixes
  (partial css {:vendors     ["webkit" "mos" "ms"]
                :auto-prefix #{:flex :flex-grow :flex-shrink :flex-direction :align-content :align-self :justify-content :align-items :flex-basis}}))

(defn wrap-f [x]
  (if (fn? x)
    x
    (fn [] x)))


