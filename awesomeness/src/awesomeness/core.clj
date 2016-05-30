(ns awesomeness.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

; (def mydriver (set-driver! {:browser :htmlunit}))
; (.setJavascriptEnabled (:webdriver mydriver) true)

; (take-screenshot :file "screenshot.png")
; 
; IllegalArgumentException No matching method found: getScreenshotAs for class org.openqa.selenium.htmlunit.HtmlUnitDriver
;   clojure.lang.Reflector.invokeMatchingMethod (Reflector.java:53)
