(ns interview-clj.core
  (:use flatland.ordered.set)
  (:gen-class))

(defn- filt1
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

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (filt "aabcb")))
