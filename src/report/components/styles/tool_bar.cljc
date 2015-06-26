(ns report.components.styles.tool-bar
  (:require [report.components.styles.params :as p]
            [report.components.styles.fonts :as f]
            [report.components.styles.core :as sc]
            [report.components.styles.color-themes :as ct]
            [report.components.styles.main-containers :as mc]
            [report.utils.log :refer [log log-o]]
            [garden.units :refer [px pt]]
            [garden.color :as c]))


(def tool-bar ^:css [mc/tool-bar
                    sc/default-shadow
                     {:padding [[0 (px (get p/h-margin :m))]]
                      :font-size (px (get p/font-sizes :1))
                      :z-index (get p/z-level :bar)}])

(def tool-bar__item--disabled ^:css {:opacity (get p/opacity :disabled-items)})

(def tool-bar__item ^:css {:display "inline-block"
                           :margin  [[0 (px (get p/h-margin :s))]]})

(def tool-bar__item--clickable ^:css {:cursor "pointer"})



(defonce init
         (let [ns-name (name (namespace ::x))
               interns (ns-interns 'report.components.styles.tool-bar)
               ;_ (log-o "interns " interns)
               css-classes (report.components.common.utils/mk-ns-classes interns)]
           (report.components.common.utils/add-style! (report.components.common.utils/css-w-prefixes {:pretty-print? true} css-classes))
           (log (str ns-name " ... initialized"))))
