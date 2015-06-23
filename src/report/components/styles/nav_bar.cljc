(ns report.components.styles.nav-bar
  (:require [report.components.styles.params :as p]
            [report.components.styles.fonts :as f]
            [report.components.styles.core :as c]
            [report.components.styles.color-themes :as ct]
            [report.components.styles.main-containers :as mc]
            [report.utils.log :refer [log log-o]]
            [garden.units :refer [px pt]]))


(def status-marker-block-width 160)

(def nav-bar ^:css [mc/nav-bar
                    c/default-shadow
                    {:z-index (get p/z-level :bar)}])


(def nav-bar__content ^:css {:height  "100%"
                             :padding 0})

(defn nav-bar__content__marker [color]
  (list [:&:before {:position      "absolute"
                    :content       "\" \""
                    :bottom        0
                    :left          0
                    :right         0
                    :width         0
                    :border-bottom [(list (px (* status-marker-block-width 2.5)) "solid" color)]
                    :border-right  [(list (px status-marker-block-width) "solid" "transparent")]}]
        [:&:after {:position "absolute"
                   :content  "\" \""
                   :z-index  99
                   :top      0
                   :bottom   0
                   :left     0
                   :width    (px 30)}]))


(def app-bar__content--success-marker
  (with-meta (nav-bar__content__marker (get p/purpose-colors :success)) {:css true}))

(def app-bar__content--semi-success-marker
  (with-meta (nav-bar__content__marker (get p/purpose-colors :semi-success)) {:css true}))

(def app-bar__content--error-marker
  (with-meta (nav-bar__content__marker (get p/purpose-colors :error)) {:css true}))

(def app-bar__content--semi-error-marker
  (with-meta (nav-bar__content__marker (get p/purpose-colors :semi-error)) {:css true}))

(def app-bar__content--neutral-marker
  (with-meta (nav-bar__content__marker (get p/purpose-colors :neutral)) {:css true}))

(def app-bar__content--accent-marker
  (with-meta (nav-bar__content__marker (get p/purpose-colors :accent)) {:css true}))

(def app-bar__content--semi-accent-marker
  (with-meta (nav-bar__content__marker (get p/purpose-colors :semi-accent)) {:css true}))

(defonce init
         (let [ns-name (name (namespace ::x))
               interns (ns-interns 'report.components.styles.nav-bar)
               ;_ (log-o "interns " interns)
               css-classes (report.components.common.utils/mk-ns-classes interns)]
           (report.components.common.utils/add-style! (report.components.common.utils/css-w-prefixes {:pretty-print? true} css-classes))
           (log (str ns-name " ... initialized"))))