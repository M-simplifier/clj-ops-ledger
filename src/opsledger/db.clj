(ns opsledger.db
  (:require [malli.core :as m]))

(def Entry
  [:map
   [:id string?]
   [:title string?]
   [:owner string?]
   [:status [:enum :triage :active :done]]
   [:risk [:enum :low :medium :high]]
   [:note string?]])

(def entries
  (atom [{:id "ops-101"
          :title "Invoice import review"
          :owner "Mina"
          :status :triage
          :risk :medium
          :note "Check duplicate vendor rows before close."}
         {:id "ops-102"
          :title "Support backlog sweep"
          :owner "Kai"
          :status :active
          :risk :high
          :note "Needs escalation path by Friday."}
         {:id "ops-103"
          :title "Weekly controls packet"
          :owner "Noa"
          :status :done
          :risk :low
          :note "Ready for archive."}]))

(defn valid-entry? [entry]
  (m/validate Entry entry))

(defn status-summary [items]
  (let [counts (frequencies (map :status items))]
    {:total (count items)
     :triage (get counts :triage 0)
     :active (get counts :active 0)
     :done (get counts :done 0)}))

(defn visible-entries [status]
  (let [items @entries]
    (if (nil? status)
      items
      (filter #(= status (:status %)) items))))

(defn dashboard []
  (let [items @entries]
    {:entries items
     :summary (status-summary items)}))

(defn create-entry! [entry]
  (let [entry (assoc entry :id (str "ops-" (+ 100 (inc (count @entries)))))]
    (if-not (valid-entry? entry)
      {:error "Invalid entry"}
      (do
        (swap! entries conj entry)
        (dashboard)))))

(defn set-status! [id status]
  (swap! entries
         (fn [items]
           (mapv #(if (= id (:id %))
                    (assoc % :status status)
                    %)
                 items)))
  (dashboard))
