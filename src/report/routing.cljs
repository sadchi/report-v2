(ns report.routing
  (:require [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [reagent.core :as r]
            [report.utils.log :refer [log log-o]]
            [report.test-results.path :refer [safe-path desafe-path]]
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



(defn- log-n-return [x]
  (log-o "xx " x)
  x)

(defn path->uri [path]
  (->> path
       (mapv encode-vec)
       (mapv safe-path)
       (mapv js/encodeURI)
       (mapv js/encodeURIComponent)
       (string/join "/")
       (str "#/")))

(defn- uri->path [uri]
  ;(log-o "uri: " uri)
  (->> (string/split uri #"/")
       ;(log-n-return)
       (mapv js/decodeURIComponent)
       ;(log-n-return)
       (mapv js/decodeURI)
       ;(log-n-return)
       (mapv desafe-path)
       ;(log-n-return)
       (mapv decode-vec)))

(secretary/set-config! :prefix "#")


(defprotocol NavPositionOps
  (get-path [this])
  (get-query-params [this]))

(defrecord NavPosition [path query-params]
  NavPositionOps
  (get-path [_] path)
  (get-query-params [_] query-params))

(defroute "/*" {uri :* params :query-params}
          (reset! nav-position (NavPosition. (uri->path uri) params)))

(let [h (History.)]
  (goog.events/listen h EventType.NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h
    (.setEnabled true)))
