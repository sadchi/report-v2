(ns report.components.status-filter
  (:require [report.components.buttons :refer [state-button]]
            [report.components.badges :refer [badged-text]]
            [report.utils.log :refer [log log-o]]))


(defn init-a-filter [statuses a-val]
  (let [status-filter-map (reduce #(assoc %1 %2 #{:show}) {} statuses)]
    (reset! a-val status-filter-map)))

(defn- switch-visibility [status-key coll]
  (let [old-val (get coll status-key)
        new-val (cond
                  (contains? old-val :show) (-> (conj old-val :hide) (disj :show))
                  (contains? old-val :hide) (-> (conj old-val :show) (disj :hide))
                  :else old-val)]
    (assoc coll status-key new-val)))

(defn- visibility->bool [x]
  (cond
    (not (set? x)) true
    (contains? x :show) true
    (contains? x :hide) false
    :else true))

(defn- w-a-active? [status val]
  (let [status-key (keyword status)
        visibility (get val status-key)]
    (visibility->bool visibility)))

(defn- active? [status a-val]
  (w-a-active? status @a-val))

(defn hovered? [status val]
  (let [status-keyword (keyword status)
        status-val (get val status-keyword)]
    (contains? status-val :hovered)))


(defn any-active? [statuses filter-val]
  (let [statuses-processed (map #(if (keyword? %) % (keyword %)) statuses)
        visibilities (map #(visibility->bool (get filter-val %)) statuses-processed)]
    (reduce #(or %1 %2) false visibilities)))

(defn mouse-enter [status filter-a]
  ;(log-o "enter " status)
  (let [status-keyword (keyword status)
        update-f (fn [x]
                   (let [old (get x status-keyword)
                         new (conj old :hovered)]
                     (assoc x status-keyword new)))]
    (swap! filter-a update-f)))

(defn mouse-leave [status filter-a]
  ;(log-o "leave " status)
  (let [status-keyword (keyword status)
        update-f (fn [x]
                   (let [old (get x status-keyword)
                         new (disj old :hovered)]
                     (assoc x status-keyword new)))]
    (swap! filter-a update-f)))


(defn status-state [{:keys [active? text on-click-f on-mouse-enter-f on-mouse-leave-f]}]
  (let [extra-class (when-not (active?) "list-column--shadowed")]
    [:div.list-column.list-column--clickable.list-column--stretch.list-column--separator
     {:class extra-class :on-click on-click-f :on-mouse-enter on-mouse-enter-f :on-mouse-leave on-mouse-leave-f} [:span.list-column__rotated-content text]]))

(defn status-filter [statuses status-map a-filter-val]
  [:div.list-row__group
   (for [[idx status] (map-indexed vector statuses)
         :let [cnt (get status-map status)]]
     ^{:key idx} [status-state {:active?    #(active? status a-filter-val)
                                :text       (name status)
                                :on-mouse-enter-f #(mouse-enter status a-filter-val)
                                :on-mouse-leave-f #(mouse-leave status a-filter-val)
                                :on-click-f #(swap! a-filter-val (partial switch-visibility status))}])])
