(defproject one-fish-spec-fish "0.1.0-SNAPSHOT"
  :description "Following a blog and practicing clojure.spec"
  :url "http://gigasquidsoftware.com/blog/2016/05/29/one-fish-spec-fish/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha3"]
                 [org.clojure/test.check "0.9.0"]]
  :main ^:skip-aot one-fish-spec-fish.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
