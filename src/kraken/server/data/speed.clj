(ns kraken.server.data.speed
  (:refer-clojure :exclude [get])
  (:require [taoensso.carmine :as carmine]))

(def conn
  {:pool {}
   :spec {:host "127.0.0.1"
          :port 6379}})

(defmacro wcar* [& body]
  `(carmine/wcar conn ~@body))

(defn get [k]
  (wcar* (carmine/get k)))

(defn set! [k v]
  (wcar* (carmine/set k v)))

(defn del! [k]
  (wcar* (carmine/del k)))
