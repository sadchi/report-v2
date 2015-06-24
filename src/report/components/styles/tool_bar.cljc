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
                    {:z-index (get p/z-level :bar)}])

(defonce init
         (let [ns-name (name (namespace ::x))
               interns (ns-interns 'report.components.styles.tool-bar)
               ;_ (log-o "interns " interns)
               css-classes (report.components.common.utils/mk-ns-classes interns)]
           (report.components.common.utils/add-style! (report.components.common.utils/css-w-prefixes {:pretty-print? true} css-classes))
           (log (str ns-name " ... initialized"))))
