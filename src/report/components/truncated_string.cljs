(ns report.components.truncated-string
  (:require [reagent.core :as r]
            [report.utils.log :refer [log log-o]]))

(def ^:priivate truncate-step 20)

(defn overflow? [this]
  (let [dom-this (r/dom-node this)
        ;_ (log-o "dom-this " dom-this)
        parent (.-parentNode dom-this)
        ;_ (log-o "parent " parent)
        this-width (.-offsetWidth dom-this)
        ;_ (log-o "this-width " this-width)
        parent-width (.-clientWidth parent)
        ;_ (log-o "parent-width " parent-width)
        ]
    (> this-width parent-width)))

(defn check-n-truncate [str-a this]
  (when (overflow? this)
    (reset! str-a (str "\u2026"(subs @str-a truncate-step)))))

(defn truncated-string [s]
  (let [final-string (r/atom s)]
    (r/create-class {:component-did-mount  (partial check-n-truncate final-string)
                     :component-did-update (partial check-n-truncate final-string)
                     :component-function   (fn []
                                             [:span @final-string])})))
