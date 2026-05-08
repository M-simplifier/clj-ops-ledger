(ns opsledger.main
  (:gen-class)
  (:require [integrant.core :as ig]
            [opsledger.system :as system]))

(defn -main [& _]
  (ig/init system/config)
  (println "clj-ops-ledger listening on http://localhost:8080"))
