(ns report.components.block-link
  (:require [report.components.common.utils :as u]
            [report.utils.log :refer [log-o]]))


(defn block-link [& {:keys [href sub-items]}]
  (fn []
    [:a (u/attr {:classes 'l/neu-custom-block-link
                 :href    href})
     (for [[idx item] (map-indexed vector sub-items)]
       ^{:key idx} [(u/wrap-f item)])]))
