(ns report.test-results.extra-params)

(def build-name
  (try
    (js->clj js/build_name)
    (catch js/Error _ "Home")))

(def artifact-base
  (try
    (js->clj js/artifacts)
    (catch js/Error _ "")))

(def artifact-abs-prefix
  (try
    (js->clj js/artifacts_abs_prefix)
    (catch js/Error _ "file://")))

(def fail-mapping
  (try
    (js->clj js/fail_mapping)
    (catch js/Error _ nil)))
