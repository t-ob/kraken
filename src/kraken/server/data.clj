(ns kraken.server.data
  (:require [kraken.server.data.batch :as batch]
            [kraken.server.data.speed :as speed]))

(require '[clj-time.core :as t]
         '[clj-time.format :as f]
         '[clj-time.coerce :as tc])

(def test-event-1
  {:event-family :tweet
   :event-type   :create
   :id           "com.twitter/123456334"
   :message      "This is a tesbt tweet"
   :created-time 1381848659376})

(def test-event-2
  {:event-family :tweet
   :event-type   :update
   :id           "com.twitter/123456334"
   :message      "This is a test tweet lol mispelled"
   :updated-time 1382021494462})

(def test-event-3
  {:event-family :tweet
   :event-type   :delete
   :id           "com.twitter/123456334"
   :updated-time 1382109366991})

(defn create-tweet [event]
  (if-let [timestamp (:created-time event)]
    (vector timestamp
            (dissoc event :event-family :event-type))))

(defn update-tweet [id event]
  (if-let [timestamp (:updated-time event)]
    (vector timestamp
            (let [[_ tweet] (speed/get id)]
              (merge tweet
                     (dissoc event :event-family :event-type))))))

(defn write-event! [{:keys [event-type id] :as event}]
  (let [speed-write! (condp = event-type
                       :create (comp (partial speed/set! id)
                                     create-tweet)
                       :update (comp (partial speed/set! id)
                                     (partial update-tweet id))
                       :delete (comp speed/del!
                                     :id))]
    (batch/write-event! event)
    (speed-write! event)))

#_(write! test-event-2)

#_(speed/get "com.twitter/12345639999")


