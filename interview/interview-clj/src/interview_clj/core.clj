(ns interview-clj.core
  (:require [clojure.core.reducers :as r])
  (:use flatland.ordered.set)
  (:gen-class))

(defn filt
  [input-list]
  (loop [input-list input-list
        seen-once (ordered-set)
        seen-more (ordered-set)]
    (let [[cur & rest] input-list
          once (cond (some #{cur} seen-more) seen-once
                      (some #{cur} seen-once) (disj seen-once cur)
                      true (conj seen-once cur))
          more (cond (some #{cur} seen-once) (conj seen-more cur)
                      true seen-more)]
        (if-not (seq rest)
          (first once) ; the final answer is: the first of seen-once
          (recur rest once more)))))

(defn doit [] (filt "aabcb"))

(defn filt1
  "Input: a list, a set of seen once, and a set of seen more.
  Takes an item off the list and tracks in the seen-once/more sets.
  The answer will be (first seen-once)"
  [input-list seen-once seen-more]
  (let [[cur & rest] input-list
        once (cond (some #{cur} seen-more) seen-once
                    (some #{cur} seen-once) (disj seen-once cur)
                    true (conj seen-once cur))
        more (cond (some #{cur} seen-once) (conj seen-more cur)
                    true seen-more)]
    (list rest once more)))

; "A reducing function
; accepts whatever, and the next piece of input,
; and returns whatever."

(defn filt2
  "Input: a map with a seen-once ordered set and a seen-more set
  and the next unit to be examined
  Output: a map with updated seen-once set and seen-more set"

; produce an identity value for clojure.core.reducers/reduce
  ([] 
  {:seen-once (ordered-set) :seen-more #{}})

  ([state-map in]
  (let [seen-once (:seen-once state-map)
        seen-more (:seen-more state-map)
        new-once (cond (some #{in} seen-more) seen-once
                    (some #{in} seen-once) (disj seen-once in)
                    true (conj seen-once in))
        new-more (cond (some #{in} seen-once) (conj seen-more in)
                    true seen-more)]
    {:seen-once new-once
    :seen-more new-more})))

(defn doit2 []
  (let [startmap {:seen-once (ordered-set) :seen-more #{}}]
    (filt2 (filt2 (filt2 (filt2 (filt2 startmap "a") "a") "b") "c") "b")))

; Must give an initial value.
(defn doit2reduce []
  (let [startmap {:seen-once (ordered-set) :seen-more #{}}]
    (reduce filt2 startmap "aabcb")))

; Will call (filt2) with no arguments to get an initial value.
(defn doit2reducer []
  (r/reduce filt2 "aabcb"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (filt "aabcb")))
