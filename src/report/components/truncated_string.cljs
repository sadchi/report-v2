(ns report.components.truncated-string
  (:require [reagent.core :as r]))

(def ^:priivate truncate-step 5)

(defn check-n-truncate [this])

(defn truncated-string [s]
  (let [final-string (r/atom s)
        ]
    (r/create-class {:component-did-mount check-n-truncate
                     :component-did-update check-n-truncate
                     :component-function (fn []
                                           [:span @final-string])

                     })))
