(ns report.components.styles.links
  (:require [report.components.styles.params :as p]
            [report.components.styles.fonts :as f]
            [report.components.styles.core :as sc]
            [report.components.styles.color-themes :as ct]
            [report.components.styles.main-containers :as mc]
            [report.utils.log :refer [log log-o]]
            [garden.units :refer [px pt]]
            [garden.color :as c]))


(def neu-custom-block-link ^:css [sc/flex-box
                                  {:width       "100%"
                                   :height      "100%"
                                   :position    "relative"
                                   :align-items "center"}
                                  [:&:hover:after
                                   {:position   "absolute"
                                    :content    "\" \""
                                    :top        0
                                    :bottom     0
                                    :left       0
                                    :right      0
                                    :opacity    0.1
                                    :background (get p/purpose-colors :accent)}]])

(defonce init
         (let [ns-name (name (namespace ::x))
               interns (ns-interns 'report.components.styles.links)
               ;_ (log-o "interns " interns)
               css-classes (report.components.common.utils/mk-ns-classes interns)]
           (report.components.common.utils/add-style! (report.components.common.utils/css-w-prefixes {:pretty-print? true} css-classes) :ns ns-name)
           (log (str ns-name " ... initialized"))))
