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
                               :optimizations :whitespace
                               :pretty-print  true}}}}
  :profiles {:dev
             {:plugins      [[com.cemerick/austin "0.1.6"]]
              :source-paths ["src"]
              :cljsbuild    {:builds {:reporter
                                      {:compiler
                                       {:preamble ["reagent/react.js"]}}}}}
             :prod
             {:cljsbuild {:builds
                          {:reporter
                           {:compiler {:preamble      ["reagent/react.min.js"]
                                       :optimizations :advanced
                                       :pretty-print  false}}}}}}

  :aliases {"bj"     ["do" ["cljsbuild" "once"]]
            "bjprod" ["with-profile" "prod" "cljsbuild" "once"]})
