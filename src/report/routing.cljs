(ns report.routing
  (:require [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [reagent.core :as r]
            [report.utils.log :refer [log log-o]]
            [clojure.string :as string])
  (:import goog.History
           goog.history.EventType))


(def nav-position (r/atom nil))


(defn- encode-vec [x]
  (if (vector? x)
    (str "vec+" (string/join "+" x))
    x))

(defn- decode-vec [x]
  (if (= "vec+" (subs x 0 4))
    (string/split (subs x 4) #"\+")
    x))

(defn path->uri [path]
  (->> path
       (map encode-vec)
       (map js/encodeURI)
       (map js/encodeURIComponent)
       (string/join "/")
       (str "#/")))

(defn- uri->path [uri]
  (->> (string/split uri #"/")
       (map js/decodeURIComponent)
       (map js/decodeURI)
       (map decode-vec)))

(secretary/set-config! :prefix "#")

(defroute "/*" {uri :*}
  (reset! nav-position (uri->path uri)))

(let [h (History.)]
  (goog.events/listen h EventType.NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h
    (.setEnabled true)))
