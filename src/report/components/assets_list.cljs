(ns report.components.assets-list
  (:require [report.utils.log :refer [log log-o]]
            [report.utils.net :refer [is-absolute-url? clean-url-scheme]]
            [clojure.string :as str]
            [report.test-results.extra-params :refer [artifact-abs-prefix artifact-base]]))





(defn- combine-url-parts [base asset-uri]
  (let [
        scheme-pattern (re-pattern "^(\\w+://+)(.*)")
        ;re-res (drop 1 (first (re-seq #"^(\w+://+)(.*)" base)))
        ;_ (log-o "re-res " re-res)
        ;scheme (first re-res)
        ;uri-rest (second re-res)
        [scheme uri-rest ] (if (re-find scheme-pattern base)
                             (drop 1 (first (re-seq scheme-pattern base)))
                             [nil base])
        _ (log-o "scheme " scheme)
        _ (log-o "uri-rest " uri-rest)
        base-list (str/split uri-rest #"/")
        _ (log-o "base " base-list)
        asset-uri-list (str/split asset-uri #"/")
        _ (log-o "asset-uri-list " asset-uri-list)
        combi-list (filter (complement str/blank?) (into base-list asset-uri-list))
        _ (log-o "combi-list " combi-list)
        ]
    (str scheme (str/join "/" combi-list))))


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
                  _ (log-o "asset-link " asset-link)
                  asset-link-cleaned (if (is-absolute-url? asset-link)
                                       (clean-url-scheme asset-link)
                                       asset-link)
                  _ (log-o "asset-link-cleaned " asset-link-cleaned)
                  asset-subtype (get asset :subtype)
                  _ (log-o "asset subtype " asset-subtype)
                  asset-link-combined (if (= "abs" asset-subtype)
                                        (combine-url-parts artifact-abs-prefix asset-link-cleaned)
                                        (combine-url-parts artifact-base asset-link-cleaned))
                  _ (log-o "asset-link-combined " asset-link-combined)
                  ]]
        ^{:key idx} [:tr.simple-table__tr {:class extra-class}
                     [:td.simple-table__td asset-name]
                     [:td.simple-table__td [:a.common-link {:href asset-link-combined} asset-link-combined]]])]]))
