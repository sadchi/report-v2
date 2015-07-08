(ns report.components.styles.core
  (:require [garden.core :refer [css]]
            [garden.units :refer [px]]
            [report.utils.log :refer [log log-o]]
            [report.components.common.utils :as u :refer [add-style!]]
            [report.components.styles.params :as p]))

(defn accent-shadow []
  (let [s-color (u/rgba (get p/purpose-colors :accent) 0.6)
        ;_ (log-o "s-color " s-color)
        s-props (str "0px 0px 2px 0px " s-color)
        ;_ (log-o "s-props " s-props)
        ]
    {:box-shadow s-props}))


(def default-shadow {:box-shadow "0px 0px 10px 0px rgba(0,0,0,0.26)"})

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

(def flex-box
  (list {:display "-webkit-flex"}
        {:display "-moz-flex"}
        {:display "-ms-flexbox"}
        {:display "-ms-flex"}
        {:display "flex"}))


(def hor-sub-block-s (with-meta (hor-sub-block :s) {:css true}))
(def hor-sub-block-m (with-meta (hor-sub-block :m) {:css true}))
(def hor-sub-block-l (with-meta (hor-sub-block :l) {:css true}))


(def nowrap-white-space ^:css {:white-space "nowrap"})


(defonce init
         (let [ns-name (name (namespace ::x))
               interns (ns-interns 'report.components.styles.core)
               ;_ (log-o "interns " interns)
               css-classes (report.components.common.utils/mk-ns-classes interns)]
           (report.components.common.utils/add-style! (report.components.common.utils/css-w-prefixes {:pretty-print? true} css-classes) :ns ns-name)
           (log (str ns-name " ... initialized"))))
