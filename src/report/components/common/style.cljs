(ns report.components.common.style
  (:require [garden.core :refer [css]]
            [garden.units :refer [px]]
            [report.utils.log :refer [log log-o]]
            [report.components.common.utils :as u :refer [add-style!]]
            [report.components.common.params :as p])
  (:require-macros [report.macros.core :refer [def-css-class get-css-desc get-css-class-names classes]]))

(defn accent-shadow []
  (let [s-color (u/rgba (get p/purpose-colors :accent) 0.6)
        ;_ (log-o "s-color " s-color)
        s-props (str "0px 0px 2px 0px " s-color)
        ;_ (log-o "s-props " s-props)
        ]
    {:box-shadow s-props}))


(def disable-hightlight
  {:-webkit-touch-callout "none"
   :-webkit-user-select   "none"
   :-moz-user-select      "none"
   :-ms-user-select       "none"
   :user-select           "none"})

(def iconic-font
  {:font-family     "fontello"
   :font-style      "normal"
   :font-weight     "normal"
   :speak           "none"
   :display         "inline-block"
   :text-decoration "inherit"
   ;:width           "1em"
   :text-align      "center"})


(defn hor-sub-block [margin-size]
  {:margin-left (px (get p/h-margin margin-size))})


(def temp-class1 {:display "none"})
(def temp-class2 {:display "none"})
(log-o "temp-class" (get-css-desc temp-class1 temp-class2))
(log-o "temp-class-names|" (classes temp-class1 temp-class2))


(def flex-box
  (list {:display "-webkit-flex"}
        {:display "-moz-flex"}
        {:display "-ms-flexbox"}
        {:display "-ms-flex"}
        {:display "flex"}))

(defonce ^:private styles
  [
   [:.hor-sub-block-s (hor-sub-block :s)]
   [:.hor-sub-block-m (hor-sub-block :m)]
   [:.hor-sub-block-l (hor-sub-block :l)]
   ])






(defonce init
  (let [name (namespace ::x)]
    (add-style! (u/css-w-prefixes {:pretty-print? true} styles))
    (log (str name " ... initialized"))))