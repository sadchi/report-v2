(ns report.components.app-bar
  (:require [report.test-results.statuses :as statuses]
            [report.utils.net :refer [set-href!]]
            [report.routing :refer [path->uri]]
            [report.test-results.path :refer [path->str]]
            [report.utils.log :refer [log-o]]))


(defn- bread-crumbs-item [path]
  (log-o "path: " path)
  [:span.breadcrumbs__item {:on-click #(set-href! (path->uri path))} (path->str (peek path))])

(defn- bread-crumbs [path]
  ;(log-o "path: " path)
  [:span
   (when-not (empty? path)
     (->> (loop [p (vec path)
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
          status (get-status-fn path)
          extra-class (condp = (statuses/evaluate-status status)
                        :good "success-back-theme"
                        :bad "error-back-theme"
                        "neutral-back-theme")]
      [:div.app__bar {:class extra-class}
       [:div.breadcrumbs
        (if (= 0 (count path))
          status
          [bread-crumbs path])]])))