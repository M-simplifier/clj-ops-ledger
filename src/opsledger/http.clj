(ns opsledger.http
  (:require [opsledger.db :as db]
            [reitit.ring :as ring]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :as response]))

(defn ok [body]
  (response/response body))

(defn create-entry [request]
  (let [entry (:body request)]
    (ok (db/create-entry! entry))))

(defn set-status [request]
  (let [id (get-in request [:path-params :id])
        status (keyword (get-in request [:body :status]))]
    (ok (db/set-status! id status))))

(def app
  (-> (ring/ring-handler
       (ring/router
        [["/" {:get (fn [_]
                      (-> (response/resource-response "public/index.html")
                          (response/content-type "text/html")))}]
         ["/api/dashboard" {:get (fn [_] (ok (db/dashboard)))}]
         ["/api/entries" {:post create-entry}]
         ["/api/entries/:id/status" {:patch set-status}]
         ["/js/*" (ring/create-resource-handler {:root "public/js"})]]))
      (wrap-json-body {:keywords? true})
      wrap-json-response))
