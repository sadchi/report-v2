(ns report.components.app-bar
  (:require [report.test-results.statuses :as statuses]
            [report.utils.net :refer [set-href!]]
            [report.routing :refer [path->uri]]
            [report.test-results.path :refer [path->str]]
            [report.utils.log :refer [log log-o]]))


(defn- bread-crumbs-item [path]
  (log-o "path: " path)
  [:span.breadcrumbs__item {:on-click #(set-href! (path->uri path))} (path->str (peek path))])

(defn- bread-crumbs [path]
  ;(log-o "crumb path: " path)
  [:span
   (when (< 0 (count path))
     (->> (loop [p (pop (vec path))
                 acc nil]
            (if (empty? p)
              (conj acc [:span.breadcrumbs__item.icon-home {:on-click #(set-href! "#/")}])
              (recur (pop p) (conj acc (bread-crumbs-item p)))))
          (interpose [:span.breadcrumbs__item.breadcrumbs__item--cursor-auto.icon-angle-right])
          (map-indexed vector)
          (map #(with-meta (second %) {:key (first %)}))))])

(defn app-bar [get-status-fn a-nav-position]
  (fn []
    (let [path @a-nav-position
          _ (log-o "path: " path)
          status (get-status-fn path)
          _ (log "status aquired")
          extra-class (condp = (statuses/evaluate-status status)
                        :good "app-bar__content--success-marker"
                        :bad "app-bar__content--error-marker"
                        "app-bar__content--neutral-marker")]
      [:div.app-bar {:class extra-class}
       [:div.app-bar__content
        [:div.breadcrumbs
         (if (= 0 (count path))
           status
           [bread-crumbs path])]]])))