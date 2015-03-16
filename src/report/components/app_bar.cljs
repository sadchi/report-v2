(ns report.components.app-bar)


(defn- bread-crumbs [path]
  )

(defn app-bar [get-status-fn a-nav-position]
  (fn []
    (let [path  @a-nav-position
          status (get-status-fn path)
          extra-class (condp = status
                        :good "success-back-theme"
                        :bad "error-back-theme"
                        "neutral-back-theme")]
      [:div.app__bar {:class extra-class}
      [:div.circled-button [:span.icon-home]]
      [:div.circled-button.circled-button-appbar--disabled [:span.icon-menu]]
      ])))