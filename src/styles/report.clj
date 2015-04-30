(ns styles.report
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]))



(defstyles main
  [:body
   {:font-family "sans-serif"
    :font-size   (px 16)
    :line-height 1.5
    :display "flex"}])


