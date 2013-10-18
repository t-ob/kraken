(ns kraken.batch.core
  (:require [clojure.string :as str]
            [cascalog.api :refer :all]))

(require '[cascalog.cascading.io :refer [with-log-level]])

(defn read-log-line [s]
  (let [[timestamp object] (str/split s #"\t")]
    [timestamp object]))

#_(with-log-level :warn
  (?- (stdout)
      (lfs-textline "/tmp/kraken-batch.txt")))
