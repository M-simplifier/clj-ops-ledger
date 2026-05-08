(ns opsledger.client
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]
            [reagent.dom.client :as rdom]))

(def status-label
  {:triage "Triage"
   :active "Active"
   :done "Done"})

(rf/reg-event-db
 :boot
 (fn [_ _]
   {:entries []
    :summary {:total 0 :triage 0 :active 0 :done 0}
    :selected-status nil
    :draft {:title "" :owner "" :status :triage :risk :medium :note ""}}))

(rf/reg-event-fx
 :load-dashboard
 (fn [_ _]
   {:http-xhrio {:method :get
                 :uri "/api/dashboard"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:dashboard-loaded]
                 :on-failure [:request-failed]}}))

(rf/reg-event-db
 :dashboard-loaded
 (fn [db [_ payload]]
   (merge db payload)))

(rf/reg-event-db
 :request-failed
 (fn [db [_ response]]
   (assoc db :error (or (:status-text response) "Request failed"))))

(rf/reg-event-db
 :select-status
 (fn [db [_ status]]
   (assoc db :selected-status status)))

(rf/reg-sub :entries (fn [db _] (:entries db)))
(rf/reg-sub :summary (fn [db _] (:summary db)))
(rf/reg-sub :selected-status (fn [db _] (:selected-status db)))

(defn visible-entries [entries status]
  (if (nil? status)
    entries
    (filter #(= status (:status %)) entries)))

(defn status-filter []
  (let [selected @(rf/subscribe [:selected-status])
        summary @(rf/subscribe [:summary])]
    [:nav
     [:button {:class (when (nil? selected) "active")
               :on-click #(rf/dispatch [:select-status nil])}
      (str "All " (get summary :total 0))]
     (for [status [:triage :active :done]]
       ^{:key status}
       [:button {:class (when (= status selected) "active")
                 :on-click #(rf/dispatch [:select-status status])}
        (str (status-label status) " " (get summary status 0))])]))

(defn entry-row [entry]
  [:article
   [:h2 (:title entry)]
   [:p (:note entry)]
   [:dl
    [:dt "Owner"] [:dd (:owner entry)]
    [:dt "Status"] [:dd (name (:status entry))]
    [:dt "Risk"] [:dd (name (:risk entry))]]])

(defn app []
  (let [entries @(rf/subscribe [:entries])
        selected @(rf/subscribe [:selected-status])
        visible (visible-entries entries selected)]
    [:main
     [:h1 "Operations Ledger"]
     [status-filter]
     [:section
      (if (seq visible)
        (for [entry visible]
          ^{:key (:id entry)}
          [entry-row entry])
        [:p "No entries for this filter."])]]))

(defn init []
  (rf/dispatch-sync [:boot])
  (rf/dispatch [:load-dashboard])
  (rdom/render (rdom/create-root (.getElementById js/document "app")) [app]))
