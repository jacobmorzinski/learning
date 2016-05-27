(ns rock-paper-scissors.core
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic.pldb :as pldb]
            [clojure.core.logic :refer :all]))

(pldb/db-rel beats hand1 hand2)
(def facts
  (pldb/db
    [beats :rock :scissor]
    [beats :paper :rock]
    [beats :scissor :paper]))

(defn play [hand1 hand2]
  (pldb/with-db facts
    (doall
      (run* [result]
            (conde
              ((beats hand1 hand2) (== result :victory))
              ((beats hand2 hand1) (== result :loss))
              ((== hand1 hand2) (== result :tie)))))))
