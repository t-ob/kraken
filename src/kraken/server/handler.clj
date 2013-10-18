(ns kraken.server.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :refer :all]
            [compojure.route :refer :all]
            [ring.middleware.format :refer :all]
            [ring.util.response :refer [response]]

            [kraken.server.data :refer [write-event!]]
            [kraken.server.parse :refer :all]))

(defroutes in-routes
  (GET "/" []
    (response {:msg "Hiii"}))
  (GET "/:id" [id]
    (response {:msg (str "Hello, " id)}))
  (POST "/data" {:keys [body-params]}
    (if-let [object (parse-object body-params)]
      (response (write-event! object))))
  (not-found (response {:msg "Page not found"})))

(def app
  (wrap-restful-format (api in-routes)
                       :formats [:edn :json-kw]))




