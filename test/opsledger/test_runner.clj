(ns opsledger.test-runner
  (:require [clojure.test :as t]
            [opsledger.db-test]
            [opsledger.http-test]))

(defn -main []
  (let [{:keys [fail error]} (t/run-tests 'opsledger.db-test
                                          'opsledger.http-test)]
    (when (pos? (+ fail error))
      (System/exit 1))))
