(ns report.components.styles.items-list
  (:require [report.components.styles.params :as p]
            [report.components.styles.fonts :as f]
            [report.components.styles.core :as sc]
            [report.components.styles.color-themes :as ct]
            [report.components.styles.main-containers :as mc]
            [report.utils.log :refer [log log-o]]
            [garden.units :refer [px pt]]
            [garden.color :as c]))


(def neu-list-row ^:css [sc/flex-box
                         {:height        (px (get p/small-units :xxl))
                          :position      "relative"
                          :align-items   "center"
                          :margin-top    "-1px"
                          :margin-bottom "-1px"
                          :padding       [(list "0 0 0" (px (get p/small-units :s)))]
                          :border-style  "solid"
                          :border-color  "#E0E0E0"
                          :border-width  "1px 0"}])


(def neu-list-row--no-padding ^:css {:padding 0})

(def neu-list-row--hoverable ^:css [
                                    [:&:hover:after
                                     {:position   "absolute"
                                      :content    "\" \""
                                      :top        0
                                      :bottom     0
                                      :left       0
                                      :right      0
                                      :background (get p/purpose-colors :accent)
                                      :opacity    "0.1"}]])

(def neu-list-row--height-s ^:css {:height (px (get p/medium-units :s))})
(def neu-list-row--height-m ^:css {:height (px (get p/medium-units :m))})
(def neu-list-row--height-l ^:css {:height (px (get p/medium-units :l))})
(def neu-list-row--height-xl ^:css {:height (px (get p/medium-units :xl))})

(def neu-list-row--m-bottom-s ^:css {:margin-bottom (px (get p/small-units :s))})
(def neu-list-row--m-bottom-m ^:css {:margin-bottom (px (get p/small-units :m))})
(def neu-list-row--m-bottom-l ^:css {:margin-bottom (px (get p/small-units :l))})

(def neu-list-row--normal ^:css [ct/white-theme
                                 {:border-color "#E0E0E0"}])

(def neu-list-row--accent ^:css [ct/grey-theme
                                 {:border-color "#E0E0E0"
                                  :font-weight  "bold"}])

(def neu-list-row--border-less ^:css {:border-width 0})

(def neu-list-column ^:css [sc/flex-box
                            {:position        "relative"
                             :height          "100%"
                             :flex-direction  "column"
                             :justify-content "center"
                             :align-items     "center"
                             :flex-grow       0
                             :flex-shrink     0
                             :width           (px 58)
                             :flex-basis      (px 58)}])

(def neu-list-column--auto-width ^:css {:width      "auto"
                                        :flex-basis "auto"})

(def neu-list-column--width-m ^:css {:width      (px 58)
                                     :flex-basis (px 58)})


(def neu-list-column--width-l ^:css {:width      (px 90)
                                     :flex-basis (px 90)})

(def neu-list-column--width-xl ^:css {:width      (px 168)
                                      :flex-basis (px 168)})

#_(def neu-list-column--clickable ^:css [ct/grey-theme
                                       {:font-weight "bold"
                                        :cursor      "pointer"
                                        :user-select "none"}
                                       [:&:hover
                                        [:&:after
                                         {:position "absolute"}]]])

(def neu-list-column--grow ^:css {:flex-grow 1})

(def neu-list-column--more-grow ^:css {:flex-grow 2})

(def neu-list-column--left ^:css {:align-items "felx-start"})
(def neu-list-column--right ^:css {:align-items "felx-end"})


(defonce init
         (let [ns-name (name (namespace ::x))
               interns (ns-interns 'report.components.styles.items-list)
               ;_ (log-o "interns " interns)
               css-classes (report.components.common.utils/mk-ns-classes interns)]
           (report.components.common.utils/add-style! (report.components.common.utils/css-w-prefixes {:pretty-print? true} css-classes) :ns ns-name)
           (log (str ns-name " ... initialized"))))