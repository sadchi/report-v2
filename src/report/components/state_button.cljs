(ns report.components.state-button)

(defn state-button [{:keys [active? sub-items on-click-f]}]
  (fn []
    (let [extra-class (when (active?) "state-button--activated")]
      [:div.state-button {:class extra-class :on-click on-click-f} sub-items])))