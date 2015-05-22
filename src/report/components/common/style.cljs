(ns report.components.common.style
  (:require [garden.color :as color]
            [report.utils.log :refer [log log-o]]
            [goog.string.format :as gformat]))

(def unit 8)

(def colors {})

(def color-themes {

                   })


(defn rgba [hex-color alpha]
  (let [rgb-map (color/hex->rgb hex-color)
        {r :red g :green b :blue} rgb-map]
  (str "rgba(" r "," g "," b "," alpha ")")))

(def purpose-colors {
                     :accent "#2196F3"
                     })

(def control-width {:s (* unit 10)
                    :m (* unit 15)
                    :l (* unit 20)})

(def control-height {:s (* unit 2)
                     :m (* unit 3)
                     :l (* unit 4)})

(def font-family {:content "Calibri , Candara, Segoe, 'Segoe UI', Optima, Arial, sans-serif"
                  :heading "'Segoe UI', Frutiger, 'Frutiger Linotype', 'Dejavu Sans', 'Helvetica Neue', Arial, sans-serif"})


(def font-sizes-ratio 1.25)
(def font-size-base 14)
(def font-sizes
  (let [s1 (* font-size-base font-sizes-ratio)
        s2 (* s1 font-sizes-ratio)
        s3 (* s2 font-sizes-ratio)
        s4 (* s3 font-sizes-ratio)
        s5 (* s4 font-sizes-ratio)
        s6 (* s5 font-sizes-ratio)]
    {:0 font-size-base
     :1 s1
     :2 s2
     :3 s3
     :4 s4
     :5 s5
     :6 s6}))


(defn accent-shadow []
  (let [s-color (rgba (get purpose-colors :accent) 0.6)
        ;_ (log-o "s-color " s-color)
        s-props (str "0px 0px 2px 0px " s-color)
        ;_ (log-o "s-props " s-props)
        ]
    {:box-shadow s-props}))