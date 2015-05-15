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
        stored-func (atom nil)
        update-f #(check-n-truncate final-string %)
        update-f-reset (fn[x]
                         (reset! final-string s)
                         (update-f x))
        handler #(debounce (partial update-f-reset %) truncate-timeout)]
    (r/create-class {:component-did-mount    (fn [x]
                                               #_(log "mounted")
                                               (update-f x)
                                               (reset! stored-func (handler x))
                                               (.addEventListener js/window "resize" @stored-func))
                     :component-did-update   update-f
                     :component-will-unmount (fn [x]
                                               #_(log "unmounted")
                                               (.removeEventListener js/window "reksize" @stored-func))
                     :component-function     (fn []
                                               [:span @final-string])})))
