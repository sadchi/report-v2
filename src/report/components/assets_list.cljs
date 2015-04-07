(ns report.components.assets-list
  (:require [report.utils.string :refer [add-zero-spaces]]
            [clojure.string :as str]))


(def artifact-base
  (try
    (js->clj js/artifacts)
    (catch js/Error _ "")))


(defn- combine-url-parts [base asset-uri]
  (let [base-list (str/split base #"/")
        asset-uri-list (str/split asset-uri #"/")
        combi-list (into base-list asset-uri-list)]
    (str/join "/" combi-list)))


(defn assets-list [assets-coll]
  (when-not (empty? assets-coll)
    [:div.vertical-block
     [:h4 "Assets:"]
     [:table.simple-table
      [:tr.simple-table__tr
       [:th.simple-table__th "Name"]
       [:th.simple-table__th "Link"]]
      (for [[idx asset] (map-indexed vector assets-coll)
            :let [extra-class (when (odd? idx) "simple-table__tr--odd")
                  asset-name (get asset :name)
                  asset-link (get asset :value)
                  asset-link-combined (combine-url-parts artifact-base asset-link)]]
        ^{:key idx} [:tr.simple-table__tr {:class extra-class}
                     [:td.simple-table__td asset-name]
                     [:td.simple-table__td [:a {:href asset-link-combined} (add-zero-spaces asset-link-combined 10)]]])]]))
