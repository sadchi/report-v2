(ns report.components.app-bar)


(defn app-bar [sub-items]
  (fn []
    [:div.app__bar
     [:div.circled-button [:span.icon-home]]
     [:div.circled-button.circled-button-appbar--disabled [:span.icon-menu]]
     ]))