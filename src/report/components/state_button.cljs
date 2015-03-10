(ns report.components.state-button)

(defn state-button [active? text]
  (fn []
    [:div.state-button-defaults text]))
