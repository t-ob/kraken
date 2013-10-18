(ns kraken.server.data.batch
  (:require [clojure.string :as str]))

(defn with-newline [s]
  (str (str/trim-newline s) "\n"))

(defn append-to-file
  "Uses spit to append to a file specified with its name as a string, or
   anything else that writer can take as an argument.  s is the string to
   append."    
  [file-name s]
  (spit file-name (with-newline s) :append true))

(def batch-file-path "/tmp/kraken-batch.txt")

(defn event [data]
  [(System/currentTimeMillis) data])

(defn write-event! [data]
  (append-to-file batch-file-path
                  (str/join "\t" (event data))))
