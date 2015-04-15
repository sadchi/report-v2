(ns report.components.tooltip
  (:require [reagent.core :as r]
            [report.utils.log :refer [log log-o]]))

(def ^:private tooltip-atom (r/atom nil))

(defn tooltip []
  (fn []
    (let [{:keys [text x y align] :as data} @tooltip-atom
          class (str "tooltip__container--" align)]
      (when-not (empty? data)
        [:div.tooltip__container {:class class
                                  :style {:left x
                                          :top  y}}
         [:div.tooltip__content text]]
        ))))


(defn- offset [x]
  (loop [elem x
         acc [0 0]]
    ;(log-o "elem " elem)
    ;(log-o "acc " acc)
    (if (or (not elem) (= "BODY" (.-tagName elem)))
      acc
      (let [new-elem (.-offsetParent elem)
            first-acc (first acc)
            second-acc (second acc)
            left-offset (.-offsetLeft elem)
            top-offset (- (.-offsetTop elem) (.-scrollTop elem))]
        (recur  new-elem [(+ first-acc left-offset) (+ second-acc top-offset)])))))

(defn show-tooltip [text align event]
  (let [target (.-target event)
        [left top] (offset target)
        ;_ (log-o "left: " left)
        ;_ (log-o "top: " top)
        width (.-offsetWidth target)
        height (.-offsetHeight target)
        ;_ (log-o "width: " width)
        ;_ (log-o "height: " height)
        align-final (if (contains? #{:left :right :center} align) align :center)

        x (condp = align-final
            :left left
            :right (+ left width)
            :center (+ left (/ width 2)))

        y (+ top height)
        ;_ (tools/log-obj "x: " x)k
        ;_ (tools/log-obj "y: " y)
        ]

    (reset! tooltip-atom {:text  text
                          :x     x
                          :y     y
                          :align (name align-final)})))

(defn hide-tooltip []
  (reset! tooltip-atom nil))