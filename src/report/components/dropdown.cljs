(ns report.components.dropdown
  (:require [report.components.common.utils :as u :refer [add-style!]]
            [report.components.common.params :as p]
            [report.components.common.style :as cs]
            [report.utils.log :refer [log log-o]]
            [reagent.core :as r]
            [garden.core :refer [css]]
            [garden.units :refer [px pt]]))



(defonce ^:private styles
  (list
    [:.dropdown-list
     {:display      "inline-block"
      :position     "relative"
      :font-family  (get p/font-family :content)
      :font-size    (px (get p/font-sizes :0))
      :font-weight  "bold"
      :border       "1px solid grey"
      :width        (px (get p/control-width :m))
      :height       (px (get p/control-height :m))
      :line-height  (px (get p/control-height :m))
      :text-align   "left"
      :padding-left (px (* p/unit 2))
      :cursor       "pointer"
      :background   "white"}
     cs/disable-hightlight
     [:&:hover (cs/accent-shadow)]]
    [:.dropdown-list--opened :.dropdown-list--closed
     [:&:after
      {:position "absolute"
       :right    (px 0)
       :top      (px 0)
       :bottom   (px 0)
       :width    (px (get p/control-height :m))}
      cs/iconic-font
      ]
     ]
    [:.dropdown-list__items-pane
     {:position   "absolute"
      :z-index    (get p/z-level :popup)
      :top        (px (get p/control-height :m))
      :left       (px -1)
      :right      (px -1)
      :cursor     "pointer"
      :border     "1px solid grey"
      :text-align "left"
      :background "white"
      }
     cs/disable-hightlight]
    [:.dropdown-list--opened:after {:content "\"\\e81d\""}]
    [:.dropdown-list--closed:after {:content "\"\\e81c\""}]
    [:.dropdown-list--s-width {:width (px (get p/control-width :s))}]
    [:.dropdown-list--m-width {:width (px (get p/control-width :m))}]
    [:.dropdown-list--l-width {:width (px (get p/control-width :l))}]
    [:.dropdown-list--xl-width {:width (px (get p/control-width :xl))}]
    [:.dropdown-list__item
     {:position     "relative"
      :padding-left (px (* p/unit 2))}
     [:&:hover:after
      {:position   "absolute"
       :content    "\" \""
       :top        0
       :bottom     0
       :left       0
       :right      0
       :background (get p/purpose-colors :accent)
       :opacity    0.15}]]

    ))




(defn dropdown [{:keys [coll current any? select-fn width]}]
  (let [state (r/atom :closed)
        cur-selection (r/atom current)
        flip-state (fn [x]
                     ;(log-o "flip-state " x)
                     (case x
                       :closed :opened
                       :opened :closed
                       :opened))
        ;_ (log-o "traits " traits)
        final-items (if any?
                      (cons "any" coll)
                      coll)
        width-class (str "dropdown-list--" (name width) "-width")
        inner-select-fn (fn [x]
                          (reset! cur-selection x)
                          (select-fn x))
        item-name #(name (str %))]
    (fn []
      ;(log "dropdown rendered")
      (let [st @state
            opened? (= st :opened)

            state-class (if opened?
                          "dropdown-list--opened"
                          "dropdown-list--closed")
            class (str state-class " " width-class)]
        [:div.dropdown-list {:on-click #(swap! state flip-state) :class class}
         (list ^{:key 0} (item-name @cur-selection)
               (when opened?
                 ^{:key 1} [:div.dropdown-list__items-pane
                            (for [[id item] (map-indexed vector final-items)]
                              ^{:key id} [:div.dropdown-list__item {:on-click #(inner-select-fn item)} (item-name item)])]))]
        ))))


(defonce init
  (let [name (namespace ::x)]
    (add-style! (u/css-w-prefixes {:pretty-print? true} styles))
    (log (str name " ... initialized"))))