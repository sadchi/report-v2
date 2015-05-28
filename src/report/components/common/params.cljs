(ns report.components.common.params)


(def unit 8)

(def line-height (* unit 4))

(def colors {})

(def color-themes {

                   })



(def purpose-colors {
                     :accent "#2196F3"
                     })

(def control-width {:s  (* unit 10)
                    :m  (* unit 20)
                    :l  (* unit 30)
                    :xl (* unit 40)})

(def control-height {:s (* unit 2)
                     :m (* unit 3)
                     :l (* unit 4)})

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


(def z-level {
              :ground                0
              :ground-controls       3
              :popup 99
              })

(def v-margin {
               :s unit
               :m (* unit 3)
               :l (* unit 6)
               })

(def h-margin {
               :s unit
               :m (* unit 3)
               :l (* unit 6)
               })

