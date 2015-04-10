(ns report.utils.net
  (:require ))

(defn set-href! [href]
  (set! (.-href (.-location js/window)) href))

(defn is-absolute-url? [uri]
  (boolean (re-find #"\w+://" uri)))