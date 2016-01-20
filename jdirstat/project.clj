(defproject jdirstat "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License (MIT)"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.tobereplaced/nio.file "0.4.0"]]
  :main ^:skip-aot jdirstat.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
