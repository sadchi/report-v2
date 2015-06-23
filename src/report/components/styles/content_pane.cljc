(ns report.components.styles.content-pane
  (:require [report.components.styles.params :as p]
            [report.components.styles.fonts :as f]
            [report.components.styles.core :as c]
            [report.components.styles.color-themes :as ct]
            [report.components.styles.main-containers :as mc]
            [report.utils.log :refer [log log-o]]
            [garden.units :refer [px pt]]))



(def content-pane
  (with-meta (let [theme (fn [x]
                          (get (get p/color-schemes :white) x))
                   h-pad (partial get p/h-margin)
                   v-pad (partial get p/v-margin)]
               [
                mc/content-area
                c/default-shadow
                {:background   (theme :back)
                 :color        (theme :fore)
                 :border       "1px solid grey"
                 :border-color (theme :border)
                 :z-index      (get p/z-level :ground)
                 :padding (px (h-pad :m))}]) {:css true}))




(defonce init
         (let [ns-name (name (namespace ::x))
               interns (ns-interns 'report.components.styles.content-pane)
               ;_ (log-o "interns " interns)
               css-classes (report.components.common.utils/mk-ns-classes interns)]
           (report.components.common.utils/add-style! (report.components.common.utils/css-w-prefixes {:pretty-print? true} css-classes))
           (log (str ns-name " ... initialized"))))