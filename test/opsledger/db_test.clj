(ns opsledger.db-test
  (:require [clojure.test :refer [deftest is testing]]
            [opsledger.db :as db]))

(deftest validates-seeded-entries
  (testing "all seeded entries match the schema"
    (is (every? db/valid-entry? @db/entries))))

(deftest summarizes-statuses
  (testing "status summary is stable"
    (is (= {:total 3 :triage 1 :active 1 :done 1}
           (db/status-summary @db/entries)))))

(deftest filters-visible-entries
  (testing "nil status returns every entry"
    (is (= 3 (count (db/visible-entries nil)))))
  (testing "status filters entries"
    (is (= ["ops-102"]
           (mapv :id (db/visible-entries :active))))))
