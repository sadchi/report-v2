(ns macro.core)


(defmacro css-class [class-sym declaration]
  `(with-meta [(keyword (str "." class-sym)) ~declaration] {:name (str class-sym)}))