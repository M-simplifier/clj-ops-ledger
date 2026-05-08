(ns opsledger.system
  (:require [integrant.core :as ig]
            [opsledger.http :as http]
            [ring.adapter.jetty :as jetty]))

(def config
  {::server {:port 8080}})

(defmethod ig/init-key ::server [_ {:keys [port]}]
  (jetty/run-jetty http/app {:port port :join? false}))

(defmethod ig/halt-key! ::server [_ server]
  (.stop server))
