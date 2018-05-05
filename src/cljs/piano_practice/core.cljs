(ns piano-practice.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(defonce state* (r/atom {}))

(def scales ["C Maj"
             "G Maj" "D Maj" "A Maj" "E Maj" "B Maj"
             "F Maj" "B♭ Maj" "E♭ Maj" "A♭ Maj"])

(def rhythms ["Straight" "Long-Short" "Short-Long"])

(defn pick!
  []
  (swap! state* assoc
         :scale (rand-nth scales)
         :rhythm (rand-nth rhythms)))

(defn page
  [state*]
  (let [{:keys [mins secs]} (:elapsed @state*)]
    [:div.page
     [:h1 "PIANO PRACTICE"]
     [:div "Time: " mins ":" (if (<= 0 secs 9) "0") secs]
     [:div "Completed: " (:completed @state*)]
     [:h2 "Scale: " (:scale @state*)]
     [:h2 "Rhythm: " (:rhythm @state*)]
     [:button {:on-click (fn [e]
                           (swap! state* update :completed inc)
                           (pick!))}
      "DONE"]]))

(defn ^:export main
  []
  (let [start-time (js/Date.)
        tick-fn (fn [e]
                  (let [elapsed (Math/floor (/ (- (js/Date.) start-time) 1000))
                        mins (quot elapsed 60)
                        secs (rem elapsed 60)]
                    (swap! state* assoc :elapsed {:mins mins :secs secs})))]
    (tick-fn nil)
    (pick!)
    (swap! state* assoc :completed 0)
    (js/setInterval tick-fn 500)
    (r/render [page state*] (.getElementById js/document "app"))))
