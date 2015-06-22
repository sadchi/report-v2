(ns dev.user
  (:require [weasel.repl :as repl]
            [report.main]))


(if (repl/alive?)
  (.log js/console "Repl already started")
  (do
    (.log js/console "Connecting to Repl")
    (repl/connect "ws://localhost:9001")))
