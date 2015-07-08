(ns report.components.styles.color-themes
  (:require [report.components.styles.params :as p]))


(defn color-theme [theme-key]
  (let [prop (fn [x]
               (get (get p/color-schemes theme-key) x))]
    {:background   (prop :back)
     :color        (prop :fore)
     :border-color (prop :border)}))

(def dark-grey-theme
  (color-theme :dark-grey))

(def white-theme
  (color-theme :white))

(def grey-theme
  (color-theme :grey))