(ns report.components.styles.main-containers
  (:require [report.components.styles.params :as p]
            [report.components.styles.fonts :as f]
            [report.components.styles.color-themes :as ct]
            [report.utils.log :refer [log log-o]]
            [garden.units :refer [px pt]]))



(def bar (list
           ct/dark-grey-theme
           {:position     "fixed"
            :overflow     "hidden"
            :left         0
            :right        0
            :white-space  "nowrap"
            :border-style "solid"
            :border-width "1px 0"
            :height       (px p/bar-height)
            :line-height  (px p/bar-height)}))



(def bar__content ^:css {:height  "100%"
                         :padding 0})

(def app ^:css [{:width        "100%"
                 :height       "100%"
                 :border-width 0}
                f/content-font
                ct/dark-grey-theme])

(def nav-bar (list bar {:top 0}))

(def tool-bar (list bar {:bottom 0}))

(def content-area {:border-width 0
                   :position     "fixed"
                   :left         0
                   :right        0
                   :top          (px (+ p/bar-height 2))
                   :bottom       (px (+ p/bar-height 2))})


(defonce init
         (let [ns-name (name (namespace ::x))
               interns (ns-interns 'report.components.styles.main-containers)
               ;_ (log-o "interns " interns)
               css-classes (report.components.common.utils/mk-ns-classes interns)]
           (report.components.common.utils/add-style! (report.components.common.utils/css-w-prefixes {:pretty-print? true} css-classes) :ns ns-name)
           (log (str ns-name " ... initialized"))))

