(ns report.components.common.utils
  (:require [garden.color :as color]
            [garden.core :refer [css]]))



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


(def css-w-prefixes
  (partial css {:vendors ["webkit" "mos" "ms"]
                :auto-prefix #{:flex :flex-grow :flex-shrink :flex-direction :align-content :align-self :justify-content :flex-basis}}))