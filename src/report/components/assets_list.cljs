(ns report.components.assets-list
  (:require [report.utils.string :refer [add-zero-spaces]]
            [report.utils.net :refer [is-absolute-url? clean-url-scheme]]
            [clojure.string :as str]))


(def artifact-base
  (try
    (js->clj js/artifacts)
    (catch js/Error _ "")))

(def artifact-abs-prefix
  (try
    (js->clj js/artifacts_abs_prefix)
    (catch js/Error _ "file://")))


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
                  asset-link (get asset :data)
                  asset-link-cleaned (if (is-absolute-url? asset-link)
                                       (clean-url-scheme asset-link)
                                       asset-link)
                  asset-subtype (get asset :subtype)
                  asset-link-combined (if (= "abs" asset-subtype)
                                        (combine-url-parts artifact-abs-prefix asset-link-cleaned)
                                        (combine-url-parts artifact-base asset-link-cleaned))]]
        ^{:key idx} [:tr.simple-table__tr {:class extra-class}
                     [:td.simple-table__td asset-name]
                     [:td.simple-table__td [:a.common-link {:href asset-link-combined} (add-zero-spaces asset-link-combined 10)]]])]]))
