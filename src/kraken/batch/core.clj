(ns kraken.batch.core
  (:require [clojure.string :as str]
            [cascalog.api :refer :all]
            [clojurewerkz.elastisch.native :as es]))

(require '[cascalog.cascading.io :refer [with-log-level]])

(defn read-log-line [s]
  (let [[timestamp object] (str/split s #"\t")]
    (if (and timestamp object)
      (vector (Long/parseLong timestamp)
              (read-string object))
      [nil nil])))

(defn read-log [path]
  (let [source (lfs-textline path)]
    (<- [?timestamp ?id ?object]
        (source ?line)
        (read-log-line ?line :> ?timestamp ?object)
        (get ?object :id :> ?id))))

(defn twitter-event? [id]
  (and (string? id)
       (= "com.twitter"
          (first (str/split id #"\/")))))

(defbufferfn construct-tweet [tuples]
  (let [objects (map (fn [[_ object]]
                       object)
                     (sort-by (fn [[timestamp _]]
                                timestamp)
                              tuples))]
    [(apply merge objects)]))

(defn tweets [source]
  (<- [?id ?tweet]
      (source ?timestamp ?id ?object)
      (twitter-event? ?id)
      (construct-tweet ?timestamp ?object :> ?tweet)))

(defbufferfn put-tweets [tuples]
  (vector (pr-str (for [[id tweet] tuples]
                    [id tweet]))))

;; No speculative execution
(defn load-tweets! [source]
  (<- [?w ?z]
      (source ?id ?tweet)
      (rand-int 2 :> ?w)
      (put-tweets ?id ?tweet :> ?z)))

#_(with-log-level :warn
    (let [source (tweets (read-log "/tmp/kraken-batch.txt"))]
      (?- (stdout)
          (load-tweets! source))))
