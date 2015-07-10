(ns report.components.styles.color-themes
  (:require [report.components.styles.params :as p]
            [report.utils.log :refer [log log-o]]
            [garden.color :as c]))


(defn color-theme [theme-key]
  (let [prop (fn [x]
               (get (get p/color-schemes theme-key) x))]
    {:background   (prop :back)
     :color        (prop :fore)
     :border-color (prop :border)}))

(def dark-grey-theme
  (color-theme :dark-grey))

(def white-theme
  (color-theme :white))

(def grey-theme
  (color-theme :grey))


(defn mk-back-theme [primary-purpose-color]
  (let [hex-color (get p/purpose-colors primary-purpose-color)
        ;_ (log-o "hex-color " hex-color)
        ]
    {:background   hex-color
     :color        (get p/colors :white)
     :border-color (c/darken hex-color 10)}))

(def neu-success-back-theme  (with-meta (mk-back-theme :success) {:css true}))
(def neu-semi-success-back-theme  (with-meta (mk-back-theme :semi-success) {:css true}))
(def neu-fail-back-theme  (with-meta (mk-back-theme :fail) {:css true}))
(def neu-semi-fail-back-theme  (with-meta (mk-back-theme :semi-fail) {:css true}))
(def neu-warning-back-theme  (with-meta (mk-back-theme :warning) {:css true}))
(def neu-semi-warning-back-theme  (with-meta (mk-back-theme :semi-warning) {:css true}))
(def neu-neutral-back-theme  (with-meta (mk-back-theme :neutral) {:css true}))

(defn back-style-by-reputation [reputation]
  (get {:good        'neu-success-back-theme
        :semi-good   'neu-semi-success-back-theme
        :bad         'neu-fail-back-theme
        :semi-bad    'neu-semi-fail-back-theme
        :accent      'neu-warning-back-theme
        :semi-accent 'neu-semi-warning-back-theme
        :neutral     'neu-neutral-back-theme} reputation))


(defonce init
         (let [ns-name (name (namespace ::x))
               interns (ns-interns 'report.components.styles.color-themes)
               ;_ (log-o "interns " interns)
               css-classes (report.components.common.utils/mk-ns-classes interns)]
           (report.components.common.utils/add-style! (report.components.common.utils/css-w-prefixes {:pretty-print? true} css-classes) :ns ns-name)
           (log (str ns-name " ... initialized"))))
