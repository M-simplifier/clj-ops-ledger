(ns opsledger.http-test
  (:require [clojure.test :refer [deftest is testing]]
            [opsledger.http :as http]
            [ring.mock.request :as mock]))

(deftest dashboard-endpoint
  (testing "dashboard returns entries"
    (let [response (http/app (mock/request :get "/api/dashboard"))]
      (is (= 200 (:status response)))
      (is (.contains (:body response) "ops-101")))))
