(defproject awesomeness "0.1.0-SNAPSHOT"
  :description "Dabbling with Clojure, Selenium-Webdriver, and PhantomJS"
  :url "http://blog.zolotko.me/2012/12/clojure-selenium-webdriver-and-phantomjs.html"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ; download phantomjs drivers from http://phantomjs.org/download.html
  ; other drivers information at
  ; https://code.google.com/p/selenium/wiki/GettingStarted
  :jvm-opts ["-Dphantomjs.binary.path=C:/phantomjs-1.9.8/phantomjs.exe"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-webdriver/clj-webdriver "0.6.0"]
                 [com.github.detro.ghostdriver/phantomjsdriver "1.0.3"]])
