(ns report.utils.net
  (:require [clojure.string :as string]))

(defn set-href! [href]
  (set! (.-href (.-location js/window)) href))

(defn is-absolute-url? [uri]
  (boolean (re-find #"\w+://" uri)))

(defn clean-url-scheme [url]
  (string/replace url #"^\w+://" ""))