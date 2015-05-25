(ns report.components.common.utils)



(defn ^:private node [tag content]
  (let [elem (.createElement js/document tag)]
    (set! (.-innerHTML elem) content)
    elem))

(defn add-style! [css]
  (.appendChild (.-head js/document) (node "style" css)))