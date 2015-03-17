(ns report.utils.net)

(defn set-href [href]
  (set! (.-href (.-location js/window)) href))
