(defproject javafx "0.1.0-SNAPSHOT"
  :description "Example of JavaFX with Clojure"
  :url "https://gist.github.com/zilti/6286307"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 ;[com.oracle/javafx-runtime "2.2.0"] ;java8 includes javafx
                 ]
  :main ^:skip-aot javafx.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
