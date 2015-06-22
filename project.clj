(defproject
  report-v2 "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [org.clojure/clojurescript "0.0-3308"]
                 [reagent "0.5.0"]
                 [secretary "1.2.1"]
                 [garden "1.2.5"]
                 [js.jquery.nicescroll "3.4.0"]]


  :jvm-opts ["-Xmx1g"]

  :plugins [[lein-modules "0.3.11"]
            [lein-cljsbuild "1.0.6"]
            [lein-haml-sass "0.2.7-SNAPSHOT"]
            [lein-garden "0.2.5"]]

  :modules {:dirs ["lib/jquery" "lib/jquery.nicescroll"]}

  :sass {:src               "sass"
         :output-directory  "target"
         :output-extension  "css"
         :delete-output-dir true}

  :cljsbuild {:builds
              {:report
               {:source-paths ["src"]
                :compiler     {:output-to     "target/report.js"
                               :output-dir    "target/out"
                               :source-map    "target/out.js.map"
                               :optimizations :none
                               :pretty-print  true}}}}

  :profiles {:dev
             {:source-paths ["src"]
              :cljsbuild    {:builds {:report
                                      {:compiler {:source-map    "target/out.js.map"
                                                  :main          "report.main"
                                                  :asset-path    "out"
                                                  :optimizations :none
                                                  :pretty-print  true}}}}}
             :dev-brepl [:dev
                         {:dependencies [[weasel "0.7.0"]
                                         [com.cemerick/piggieback "0.2.1"]]
                          :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                          :source-paths ["src" "dev/clj"]
                          :cljsbuild {:builds {:report {:source-paths ["src" "dev/cljs"]
                                                        :compiler {:main "dev.user"}}}}}]
             :prod
             {:cljsbuild {:builds
                          {:report
                           {:compiler {:optimizations :advanced
                                       :closure-warnings {:externs-validation :off
                                                          :non-standard-jsdoc :off}
                                       :pretty-print  false}}}}}}

  :aliases {"bc"     ["do" ["sass" "once"]]
            "bj"     ["do" ["cljsbuild" "auto"]]
            "bjprod" ["with-profile" "prod" "cljsbuild" "once"]
            "bjrepl" ["with-profile" "+dev-brepl" "cljsbuild" "auto"]
            "brepl"  ["with-profile" "+dev-brepl" "repl"]}


  :garden {:builds [{;; Optional name of the build:
                     :id           "main"
                     :source-paths ["src/styles"]
                     :stylesheet   styles.report/main
                     :compiler     {:output-to     "target/main.css"
                                    :pretty-print? true}}]}
  )
