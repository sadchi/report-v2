(ns report.components.styles.badges
  (:require [report.components.styles.params :as p]
            [report.components.styles.fonts :as f]
            [report.components.styles.core :as sc]
            [report.components.styles.color-themes :as ct]
            [report.components.styles.main-containers :as mc]
            [report.utils.log :refer [log log-o]]
            [garden.units :refer [px pt]]
            [garden.color :as c]))


(def sizes {:s (* 2 p/unit)
            :m (* 2.5 p/unit)
            :l (* 3 p/unit)})

(def neu-badge ^:css {:height (px (get sizes :m))
                      :line-height (px (get sizes :m))
                      :display "inline-block"
                      :padding "0 2px"
                      :border-style "solid"
                      :border-width "1px"})

(def neu-badge--small ^:css {:height (px (get sizes :s))
                             :line-height (px (get sizes :s))})


(defonce init
         (let [ns-name (name (namespace ::x))
               interns (ns-interns 'report.components.styles.badges)
               ;_ (log-o "interns " interns)
               css-classes (report.components.common.utils/mk-ns-classes interns)]
           (report.components.common.utils/add-style! (report.components.common.utils/css-w-prefixes {:pretty-print? true} css-classes) :ns ns-name)
           (log (str ns-name " ... initialized"))))
