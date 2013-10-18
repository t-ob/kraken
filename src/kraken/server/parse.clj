(ns kraken.server.parse)

(defn parse-object [object]
  (if-let [event-family (:event-family object)]
    (-> object
        (update-in [:event-family] keyword)
        (update-in [:event-type] keyword))))
