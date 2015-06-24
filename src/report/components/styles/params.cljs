(ns report.components.styles.params
  (:require [garden.color :as c]
            [report.utils.log :refer [log log-o]]))


(def unit 8)

(def colors {:red         "#F44336"
             :pink        "#E91E63"
             :indigo      "#3F51B5"
             :blue        "#2196F3"
             :light-blue  "#03A9F4"
             :cyan        "#00BCD4"
             :teal        "#009688"
             :green       "#4CAF50"
             :lime        "#CDDC39"
             :yellow      "#FFEB3B"
             :amber       "#FFC107"
             :orange      "#FF9800"
             :deep-orange "#FF5722"
             :grey        "#9E9E9E"
             :blue-grey   "#607D8B"
             :white       "#FFFFFF"})

(defn darken [color-key perc]
  (c/as-hex (c/darken (get colors color-key) perc)))
(defn lighten [color-key perc]
  (c/as-hex (c/lighten (get colors color-key) perc)))
(defn normal [color-key]
  (get colors color-key))

(def color-schemes {:dark-grey {:back   (lighten :grey 12)
                                :fore   (normal :white)
                                :border (lighten :grey 7)}
                    :white     {:back   (darken :white 5)
                                :fore   (darken :grey 24)
                                :border (darken :white 5)}})


(defn mix [color1 color2]
  (let [mixed (c/mix color1 color2)
        final-mixed (zipmap (keys mixed) (map int (vals mixed)))]
    final-mixed))

(def purpose-colors
  (let [s (normal :teal)
        n (darken :grey 5)
        f (normal :red)
        e (normal :yellow)
        w (normal :amber)
        a "#2196F3"]
    {:neutral      n
     :success      s
     :semi-success (c/rgb->hex (mix s n))
     :fail         f
     :semi-fail    (c/rgb->hex (mix f n))
     :error        e
     :semi-error   (c/rgb->hex (mix e n))
     :accent       a
     :semi-accent  (c/rgb->hex (mix a n))
     :warning      w
     :semi-warning (c/rgb->hex (mix w n))
     }))

(def control-width {:s  (* unit 10)
                    :m  (* unit 20)
                    :l  (* unit 30)
                    :xl (* unit 40)})

(def control-height {:s   (* unit 2)
                     :m   (* unit 3)
                     :l   (* unit 4)
                     :xl  (* unit 5)
                     :xxl (* unit 6)})

(def bar-height (get control-height :xxl))

(def line-height (get control-height :l))

(def font-family {:content "Calibri , Candara, Segoe, 'Segoe UI', Optima, Arial, sans-serif"
                  :heading "Calibri , Candara, Segoe, 'Segoe UI', Optima, Arial, sans-serif" ;"'Segoe UI', Frutiger, 'Frutiger Linotype', 'Dejavu Sans', 'Helvetica Neue', Arial, sans-serif"
                  })


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


(def z-level {:ground          5
              :bar             10
              :ground-controls 15
              :popup           99})

(def v-margin {:s unit
               :m (* unit 3)
               :l (* unit 6)})

(def h-margin {:s unit
               :m (* unit 3)
               :l (* unit 6)})

