(defproject
  report-v2 "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2913"]
                 [reagent "0.5.0"]
                 [secretary "1.2.1"]
                 [garden "1.2.5"]]


  :jvm-opts ["-Xmx1g"]

  :plugins [[lein-cljsbuild "1.0.5"]
            [lein-haml-sass "0.2.7-SNAPSHOT"]
            [lein-garden "0.2.5"]]

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
                               :pretty-print  true
                               :foreign-libs  [{:file     "libs/jquery-1.11.2.js"
                                                :file-min "libs/jquery-1.11.2.min.js"
                                                :provides ["jquery.main"]}
                                               {:file     "libs/jquery.nicescroll.js"
                                                :file-min "libs/jquery.nicescroll.min.js"
                                                :requires ["jquery.main"]
                                                :provides ["jquery.nicescroll"]}
                                               ]
                               :externs       ["libs/jquery-1.11.2.js" "libs/jquery.nicescroll.js"]}}}}

  :profiles {:dev
             {:source-paths ["src"]
              :cljsbuild    {:builds {:report
                                      {:compiler {:source-map    "target/out.js.map"
                                                  :main          "report.main"
                                                  :asset-path    "out"
                                                  :optimizations :none
                                                  :pretty-print  true}}}}}
             :prod
             {:cljsbuild {:builds
                          {:report
                           {:compiler {:optimizations :advanced
                                       :closure-warnings
                                                      {:externs-validation :off
                                                       :non-standard-jsdoc :off}
                                       :pretty-print  false}}}}}}

  :aliases {"bc"     ["do" ["sass" "once"]]
            "bj"     ["do" ["cljsbuild" "auto"]]
            "bjprod" ["with-profile" "prod" "cljsbuild" "once"]}


  :garden {:builds [{;; Optional name of the build:
                     :id           "main"
                     ;; Source paths where the stylesheet source code is
                     :source-paths ["src/styles"]
                     ;; The var containing your stylesheet:
                     :stylesheet   styles.report/main
                     ;; Compiler flags passed to `garden.core/css`:
                     :compiler     {;; Where to save the file:
                                    :output-to     "target/main.css"
                                    ;; Compress the output?
                                    :pretty-print? true}}]}
  )
