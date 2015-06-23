(ns report.components.styles.color-themes
  (:require [report.components.styles.params :as p]))


(def dark-greyish-theme
  (let [prop (fn [x]
               (get (get p/color-schemes :dark-grey) x))]
    {:background   (prop :back)
     :color        (prop :fore)
     :border-color (prop :border)}))