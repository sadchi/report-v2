(ns report.components.app-bar)


(defn app-bar [sub-items]
  (fn []
    [:div.app__bar
     [:div.circled-button-appbar [:span.icon-home]]
     [:div.circled-button-appbar.circled-button-appbar--disabled [:span.icon-menu]]
     ]))