(ns report.components.status-filter
  (:require [report.components.buttons :refer [state-button]]
            [report.components.badges :refer [badged-text]]
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

(defn- w-a-active? [status val]
  (let [status-key (keyword status)
        visibility (get val status-key)]
    (visibility->bool visibility)))

(defn- active? [status a-val]
  (w-a-active? status @a-val))

(defn any-active? [statuses filter-val]
  (let [statuses-processed (map #(if (keyword? %) % (keyword %)) statuses)
        visibilities (map #(visibility->bool (get filter-val %)) statuses-processed)]
    (reduce #(or %1 %2) false visibilities)))

(defn status-filter [statuses status-map a-filter-val]
  [:div.buttons-group
   (for [[idx status] (map-indexed vector statuses)
         :let [cnt (get status-map status)]]
     ^{:key idx} [state-button {:active?    #(active? status a-filter-val)
                                :sub-items  (list ^{:key 1} (str (name status) "\u2007") ^{:key 2} [badged-text status cnt true])
                                :on-click-f #(swap! a-filter-val (partial switch-visibility status))}])])
