(defproject
  report-v2 "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2913"]
                 [reagent "0.5.0-alpha3"]
                 [secretary "1.2.1"]]


  :plugins [[lein-cljsbuild "1.0.5"]]

  :cljsbuild {:builds
              {:report
               {:source-paths ["src"]
                :compiler     {:output-to     "target/report.js"
                               :output-dir    "target/out"
                               :source-map    "target/out.js.map"
                               :optimizations :none
                               :pretty-print  true}}}}

  :profiles {:dev
             {:plugins      [[com.cemerick/austin "0.1.6"]]
              :source-paths ["src"]
              :cljsbuild    {:builds {:report
                                      {:compiler {:source-map    "target/out.js.map"
                                                  :main          "report.main"
                                                  :asset-path    "out"
                                                  :optimizations :none
                                                  :pretty-print  true}}}}}
             :prod
             {:cljsbuild {:builds
                          {:report
                           {:compiler {:optimizations    :advanced
                                       :libs             ["libs/jquery-1.11.2.min.js" "libs/jquery.nicescroll.min.js"]
                                       :externs          ["libs/jquery-1.11.2.min.js" "libs/jquery.nicescroll.min.js"]
                                       :closure-warnings {:externs-validation :off
                                                          :non-standard-jsdoc :off}
                                       :pretty-print     false}}}}}}

  :aliases {"bj"     ["do" ["cljsbuild" "once"]]
            "bjprod" ["with-profile" "prod" "cljsbuild" "once"]})
