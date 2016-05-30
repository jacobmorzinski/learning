(ns one-fish-spec-fish.core
  (:require [clojure.spec :as s]))

(def fish-numbers {0 "Zero"
                   1 "One"
                   2 "Two"})

(s/def ::fish-number (set (keys fish-numbers)))

(s/valid? ::fish-number 1)
(s/valid? ::fish-number 5)

;(s/explain ::fish-number 5)

(s/def ::color #{"Red" "Blue" "Dun"})

(s/def ::first-line (s/cat :n1 ::number :n2 ::number
                           :c1 ::color :c2 ::color))

;(s/explain ::first-line [1 2 "Red" "Black"])

(defn one-bigger? [{:keys [n1 n2]}]
  (= n2 (inc n1)))

(s/def ::first-line (s/and (s/cat :n1 ::fish-number :n2 ::fish-number
                                  :c1 ::color :c2 ::color)
                           one-bigger?
                           #(not= (:c1 %) (:c2 %))))

(s/valid? ::first-line [1 2 "Red" "Blue"])

(s/conform ::first-line [1 2 "Red" "Blue"])

;(s/explain ::first-line [2 1 "Red" "Blue"])

;(s/exercise ::first-line 5)

(defn fish-number-rhymes-with-color? [{n :n2 c :c2}]
  (or
    (= [n c] [2 "Blue"])
    (= [n c] [1 "Dun"])))

(s/def ::first-line (s/and (s/cat :n1 ::fish-number :n2 ::fish-number
                                  :c1 ::color :c2 ::color)
                           one-bigger?
                           #(not= (:c1 %) (:c2 %))
                           fish-number-rhymes-with-color?))

(s/valid? ::first-line [1 2 "Red" "Blue"])
(s/valid? ::first-line [1 2 "Red" "Dun"])

;(s/exercise ::first-line)

(defn fish-line [n1 n2 c1 c2]
  (clojure.string/join " "
                       (map #(str % " fish.")
                            [(get fish-numbers n1)
                             (get fish-numbers n2)
                             c1
                             c2])))

;(fish-line 0 1 "red" "dun")

(s/fdef fish-line
        :args ::first-line
        :ret string?)

(s/instrument #'fish-line)

(fish-line 1 2 "Red" "Blue")

;(fish-line 1 2 "Red" "Dun") ;fails

(for [l (s/exercise ::first-line 5)]
  (apply fish-line (first l)))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
