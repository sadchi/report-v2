(ns report.routing
  (:require [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [reagent.core :as r]
            [report.utils.log :refer [log log-o]]
            [report.test-results.path :refer [desafe-path]]
            [clojure.string :as string])
  (:import goog.History
           goog.history.EventType))


(def nav-position (r/atom nil))


(defn- encode-vec [x]
  (if (vector? x)
    (str "vec+" (string/join "+" x))
    x))

(defn- decode-vec [x]
  ;(log-o "x: " x)
  (if (= "vec+" (subs x 0 4))
    (string/split (subs x 4) #"\+")
    x))



(defn path->uri [path]
  (->> path
       (mapv encode-vec)
       (mapv js/encodeURI)
       (mapv js/encodeURIComponent)
       (string/join "/")
       (str "#/")))

(defn- uri->path [uri]
  ;(log-o "uri: " uri)
  (->> (string/split uri #"/")
       (mapv js/decodeURIComponent)
       (mapv js/decodeURI)
       (mapv desafe-path)
       (mapv decode-vec)))

(secretary/set-config! :prefix "#")

(defroute "/*" {uri :* params :query-params}
          (reset! nav-position (with-meta (uri->path uri) params)))

(let [h (History.)]
  (goog.events/listen h EventType.NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h
    (.setEnabled true)))
