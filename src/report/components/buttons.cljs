(ns report.components.buttons)

(defn state-button [{:keys [active? sub-items on-click-f]}]
  (let [extra-class (when (active?) "state-button--activated")]
    [:div.state-button {:class extra-class :on-click on-click-f} sub-items]))


(defn button [on-click-fn text ]
  [:div.button {:on-click on-click-fn} text])