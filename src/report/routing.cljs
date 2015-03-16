(ns report.routing
  (:require [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [reagent.core :as r]
            [report.utils.log :refer [log log-o]]
            [clojure.string :as string])
  (:import goog.History
           goog.history.EventType))


(def nav-position (r/atom nil))


(defn path->uri [path]
  (->> path
       (map js/encodeURI)
       (map js/encodeURIComponent)
       (string/join "/")
       (str "#/")))

(defn- uri->path [uri]
  (->> (string/split uri #"/")
       (map js/decodeURIComponent)
       (map js/decodeURI)))

(secretary/set-config! :prefix "#")

(defroute "/*" {uri :*}
  (reset! nav-position (uri->path uri)))

(let [h (History.)]
  (goog.events/listen h EventType.NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h
    (.setEnabled true)))
