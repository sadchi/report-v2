(ns report.components.dropdown
  (:require [report.components.common.utils :refer [add-style!]]
            [report.components.common.style :as cs]
            [report.utils.log :refer [log log-o]]
            [garden.core :refer [css]]
            [garden.units :refer [px pt]]))



(defonce ^:private styles
  (list
    [:.dropdown-list
     {:display      "inline-block"
      :position     "relative"
      :font-family  (get cs/font-family :content)
      :font-size    (px (get cs/font-sizes :0))
      :font-weight  "bold"
      :border       "1px solid grey"
      :width        (px (get cs/control-width :m))
      :height       (px (get cs/control-height :m))
      :line-height  (px (get cs/control-height :m))
      :text-align   "left"
      :padding-left (px (* cs/unit 2))
      :cursor "pointer"}
     [:&:hover (cs/accent-shadow)]]
    [:.dropdown-list--opened {}]
    [:.dropdown-list--closed {}]
    [:.dropdown-list--s-width {}]
    [:.dropdown-list--m-width {}]
    [:.dropdown-list--l-width {}]
    [:.dropdown-list__item {}]

    ))




(defn dropdown [{:keys [coll current traits select-fn]}]
  [:div.dropdown-list "Test"])


(defonce init
  (let [name (namespace ::x)]
    (log (str name " ... initializing"))
    (add-style! (css {:pretty-print? false} styles))
    (log (str name " ... initialized"))))