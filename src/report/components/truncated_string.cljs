(ns report.components.truncated-string
  (:require [reagent.core :as r]
            [report.utils.log :refer [log log-o]]
            [report.test-results.curried-styles.utils :refer [overflow?]]
            [report.utils.events :refer [debounce]]))

(def ^:priivate truncate-step 10)
(def ^:private truncate-timeout 500)

(defn check-n-truncate [str-a this]
  (when (overflow? this)
    (reset! str-a (str "\u2026"(subs @str-a truncate-step)))))

(defn truncated-string [s]
  (let [final-string (r/atom s)
        update-f #(check-n-truncate final-string %)
        update-f-reset (fn[x]
                         (reset! final-string s)
                         (update-f x))]
    (r/create-class {:component-did-mount  (fn [x]
                                             (update-f x)
                                             (.addEventListener js/window "resize" (debounce (partial update-f-reset x) truncate-timeout)))
                     :component-did-update update-f
                     :component-function   (fn []
                                             [:span @final-string])})))
