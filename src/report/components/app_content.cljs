(ns report.components.app-content
  (:require [reagent.core :as r]
            [report.utils.log :refer [log log-o]]))


(defn home-view [test-data a-nav-position]
  (fn []
    [:div.list-caption "Overview"]))


(defn app-content []
  (fn []
    [:div.content-pane
     [home-view nil nil]
     [:a {:href "#/234"} "TEST1"] [:a {:href "#/123/234"} "TEST2"] [:a {:href "#/123/234/34534"} "TEST3"]
     [:a {:href "#/123/234/123/1dd/dfg"} "TEST4"]]))

(def app-content-nicescroll
  (with-meta app-content
             {:component-did-mount #(.niceScroll (js/$ (r/dom-node %)))}))