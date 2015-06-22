(require '[cemerick.piggieback :as pb]
         '[weasel.repl.websocket :as w])

(defn browser-repl []
  (pb/cljs-repl (w/repl-env :ip "127.0.0.1" :port 9001)))
