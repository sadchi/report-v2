(set-env! 
	:source-paths    #{"src"}
	:dependencies '[[adzerk/boot-cljs "0.0-3269-2" :scope "test"]
	                [reagent "0.5.0"]
                    [secretary "1.2.1"]
                    [garden "1.2.5"]])

(require '[adzerk.boot-cljs :refer :all])

#_(deftask! dev []
(cljs {:output-to     "target/report.js"
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
                               :externs       ["libs/jquery-1.11.2.js" "libs/jquery.nicescroll.js"]}))