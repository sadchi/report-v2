(ns report.test-results.curried-styles.utils
  (:require [reagent.core :as r]))




(defn dom-width [dom-this]
  (.-offsetWidth dom-this))

(defn parent-dom-width [dom-this]
  (let [parent (.-parentNode dom-this)
        parent-width (.-clientWidth parent)]
    parent-width))

(defn overflow? [this]
  (let [dom-this (r/dom-node this)
        ;_ (log-o "dom-this " dom-this)
        this-width (dom-width dom-this)
        ;_ (log-o "this-width " this-width)
        p-width (parent-dom-width dom-this)
        ;_ (log-o "parent-width " parent-width)
        ]
    (> this-width p-width)))
