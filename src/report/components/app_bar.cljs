(ns report.components.app-bar
  (:require [report.test-results.statuses :as statuses]
            [report.utils.net :refer [set-href!]]
            [report.routing :refer [path->uri]]
            [report.test-results.path :refer [path->str]]
            [report.utils.log :refer [log log-o]]
            [report.test-results.extra-params :refer [build-name]]
            [reagent.core :as r]
            [report.test-results.curried-styles.utils :refer [overflow? dom-width parent-dom-width]]
            [report.utils.events :refer [debounce]]))


(defn- bread-crumbs-item [path]
  ;(log-o "path: " path)
  [:a.custom-link.breadcrumbs__item {:href (path->uri path)} (path->str (peek path))])


(def update-timeout 500)

(defn ^:private update-f [x]
  (let [dom-x (r/dom-node x)
        ;_ (log-o "dom-x " dom-x)
        x-width (dom-width dom-x)
        ;_ (log-o "x width " x-width)
        p-x-width (parent-dom-width dom-x)
        ;_ (log-o "p x width " p-x-width)
        width-diff (- p-x-width x-width)
        ;_ (log-o "width diff" width-diff)
        f (fn [] (update-f x))]
    (set! (-> dom-x .-style .-marginLeft) "0")
    (when (< width-diff 0)
      #_(log "it's negative")
      (set! (-> dom-x .-style .-marginLeft) (str width-diff "px")))
    (.addEventListener js/window "resize" (debounce f update-timeout))))


(defn- bread-crumbs [a-nav-position status]
  ;(log-o "crumb path: " path)
  (r/create-class
    {:component-did-mount  update-f
     :component-did-update update-f
     :component-function   (fn []
                             (let [path @a-nav-position]
                               [:div.breadcrumbs
                                (if (< 0 (count path))
                                  (->> (loop [p (pop (vec path))
                                              acc nil]
                                         (if (empty? p)
                                           (conj acc [:a.custom-link.breadcrumbs__item {:href "#/"} build-name])
                                           (recur (pop p) (conj acc (bread-crumbs-item p)))))
                                       (interpose [:span.breadcrumbs__item.breadcrumbs__item--cursor-auto.icon-angle-right])
                                       (map-indexed vector)
                                       (map #(with-meta (second %) {:key (first %)})))
                                  (str build-name ": " status))]))}))

(defn app-bar [get-status-fn a-nav-position]
  (fn []
    (let [path @a-nav-position
          ;_ (log-o "path: " path)
          status (get-status-fn path)
          ;_ (log "status aquired")
          extra-class (condp = (statuses/evaluate-status status)
                        :good "app-bar__content--success-marker"
                        :bad "app-bar__content--error-marker"
                        "app-bar__content--neutral-marker")
          ;_ (log "extra class built")
          ]
      [:div.app-bar {:class extra-class}
       [:div.app-bar__content
        [bread-crumbs a-nav-position status]]])))