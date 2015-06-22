(ns report.components.dropdown
  (:require [report.components.common.utils :as u :refer [add-style!]]
            [report.components.common.params :as p]
            [report.components.common.style :as cs]
            [report.utils.log :refer [log log-o]]
            [reagent.core :as r]
            [garden.core :refer [css]]
            [garden.units :refer [px pt]])
  #_(:require-macros [report.macros.core :refer [init-styles]]))


(def dropdown-list ^:css [{:display      "inline-block"
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
                          [:&:hover (cs/accent-shadow)]])

(def dropdown-list__mark
  [:&:after
   {:position "absolute"
    :right    (px 0)
    :top      (px 0)
    :bottom   (px 0)
    :width    (px (get p/control-height :m))}
   cs/iconic-font])

(def dropdown-list--opened ^:css
[dropdown-list__mark
 [:&:after {:content "\"\\e81d\""}]])

(def dropdown-list--closed ^:css
[dropdown-list__mark
 [:&:after {:content "\"\\e81c\""}]])


(def dropdown-list__items-pane ^:css [{:position   "absolute"
                                       :z-index    (get p/z-level :popup)
                                       :top        (px (get p/control-height :m))
                                       :left       (px -1)
                                       :right      (px -1)
                                       :cursor     "pointer"
                                       :border     "1px solid grey"
                                       :text-align "left"
                                       :background "white"
                                       }
                                      cs/disable-hightlight])

(def dropdown-list__item ^:css [{:position     "relative"
                                 :padding-left (px (* p/unit 2))}
                                [:&:hover:after
                                 {:position   "absolute"
                                  :content    "\" \""
                                  :top        0
                                  :bottom     0
                                  :left       0
                                  :right      0
                                  :background (get p/purpose-colors :accent)
                                  :opacity    0.15}]])

(def dropdown-list--s-width ^:css {:width (px (get p/control-width :s))})
(def dropdown-list--m-width ^:css {:width (px (get p/control-width :m))})
(def dropdown-list--l-width ^:css {:width (px (get p/control-width :l))})
(def dropdown-list--xl-width ^:css {:width (px (get p/control-width :xl))})


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
        width-class (case width
                      :s 'dropdown-list--s-width
                      :m 'dropdown-list--m-width
                      :l 'dropdown-list--l-width
                      :xl 'dropdown-list--xl-width
                      'dropdown-list--m-width)
        inner-select-fn (fn [x]
                          (reset! cur-selection x)
                          (select-fn x))
        item-name #(name (str %))]
    (fn []
      ;(log "dropdown rendered")
      (let [st @state
            opened? (= st :opened)

            state-class (if opened?
                          'dropdown-list--opened
                          'dropdown-list--closed)
            all-classes (list 'dropdown-list width-class state-class)]
        [:div (u/attr {:classes  all-classes
                       :on-click #(swap! state flip-state)})
         (list ^{:key 0} (item-name @cur-selection)
               (when opened?
                 ^{:key 1} [:div (u/classes 'dropdown-list__items-pane)
                            (for [[id item] (map-indexed vector final-items)]
                              ^{:key id} [:div (u/attr {:classes  'dropdown-list__item
                                                        :on-click #(inner-select-fn item)})
                                          (item-name item)])]))]))))




(defonce init
  (let [ns-name (name (namespace ::x))
        interns (ns-interns 'report.components.dropdown)
        ;_ (log-o "interns " interns)
        css-classes (u/mk-ns-classes interns)]
    (add-style! (u/css-w-prefixes {:pretty-print? true} css-classes))
    (log (str ns-name " ... initialized"))))