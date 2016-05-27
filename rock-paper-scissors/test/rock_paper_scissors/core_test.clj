(ns rock-paper-scissors.core-test
  (:require [clojure.test :refer :all]
            [rock-paper-scissors.core :refer :all]))

(deftest rock-paper
  (testing "Rock vs Paper"
    (is (= '(:loss)
           (play :rock :paper)))))

(deftest rock-scissors
  (testing "Rock vs Scissors"
    (is (= '(:victory)
           (play :rock :scissor)))))

(deftest rock-rock
  (testing "Rock vs Rock"
    (is (= '(:tie)
           (play :rock :rock)))))
