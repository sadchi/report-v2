(ns report.components.status-filter
  (:require [report.components.state-button :refer [state-button]]
            [report.components.badges :refer [badged-count]]
            [report.utils.log :refer [log log-o]]))


(defn init-a-filter [statuses a-val]
  (let [status-filter-map (reduce #(assoc %1 %2 :show) {} statuses)]
    (reset! a-val status-filter-map)))

(defn- switch-visibility [status-key coll]
  (let [old-val (get coll status-key)
        new-val (case old-val
                  :show :hide
                  :hide :show)]
    (assoc coll status-key new-val)))

(defn- visibility->bool [x]
  (case x
    :show true
    :hide false))

(defn- active? [status-key a-val]
  (let [ visibility (get @a-val status-key)]
    (visibility->bool visibility)))

(defn any-active? [statuses filter-val]
  (let [visibilities (map #(visibility->bool (get filter-val %)) statuses)]
    (reduce #(or %1 %2) false visibilities)))

(defn status-filter [statuses status-map a-val]
  (fn []
    [:div.buttons-group
     (for [[idx status] (map-indexed vector statuses)
           :let [cnt  (get status-map status)]]
       ^{:key idx} [state-button {:active?    #(active? status a-val)
                                  :sub-items  (list ^{:key 1} (str (name status) "\u2007") ^{:key 2} [badged-count status cnt true])
                                  :on-click-f #(swap! a-val (partial switch-visibility status))}])]))
