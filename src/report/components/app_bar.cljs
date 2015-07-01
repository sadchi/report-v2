(ns report.components.app-bar
  (:require [report.test-results.statuses :as statuses]
            [report.utils.net :refer [set-href!]]
            [report.routing :as routing :refer [path->uri]]
            [report.test-results.path :refer [path->str]]
            [report.utils.log :refer [log log-o]]
            [report.test-results.extra-params :refer [build-name]]
            [reagent.core :as r]
            [report.test-results.curried-styles.utils :refer [overflow? dom-width parent-dom-width]]
            [report.utils.events :refer [debounce]]
            [report.components.styles.nav-bar :as n]
            [report.components.styles.main-containers :as mc]
            [report.components.common.utils :as u]))


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


(defn- bread-crumbs [a-nav-position get-status-fn]
  ;(log-o "crumb path: " path)
  (r/create-class
    {:component-did-mount  update-f
     :component-did-update update-f
     :component-function   (fn []
                             (let [path (routing/get-path @a-nav-position)
                                   root-status (get-status-fn [])]
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
                                  (str build-name "\u2007\u2007" root-status))]))}))

(defn app-bar [get-status-fn a-nav-position]
  (fn []
    (let [path (routing/get-path @a-nav-position)
          ;_ (log-o "path: " path)
          status (get-status-fn path)
          ;_ (log-o "status " status)
          extra-class (condp = (statuses/get-reputation status)
                        :good        'n/nav-bar__content--success-marker
                        :semi-good   'n/nav-bar__content--semi-success-marker
                        :bad         'n/nav-bar__content--error-marker
                        :semi-bad    'n/nav-bar__content--semi-error-marker
                        :accent      'n/nav-bar__content--accent-marker
                        :semi-accent 'n/nav-bar__content--semi-accent-marker
                        'n/nav-bar__content--neutral-marker)
          ;_ (log-o "extra class" extra-class)
          ]
      [:div
       ;.app-bar #_{:class extra-class}
       (u/attr {:classes (list 'n/nav-bar extra-class)})
       [:div (u/attr {:classes 'mc/bar__content})
        [bread-crumbs a-nav-position get-status-fn]]])))