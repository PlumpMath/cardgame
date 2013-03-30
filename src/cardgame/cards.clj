(ns cardgame.cards)

(defn get-key-for-player [game player-name]
  (let [n1 (get-in game [:p1 :name])
        n2 (get-in game [:p2 :name])]
    (cond
     (= n1 player-name) :p1
     (= n2 player-name) :p2
     :else :CANNOT-FIND-PLAYER)))

(defn log [game & messages]
  (update-in game [:messages] conj (clojure.string/join "" messages)))

(defn draw-card [game player-key]
  (let [deck (get-in game [player-key :deck])]
    (if (empty? deck)
      (log game "Out of cards in " (get-in game [player-key :name]) "'s deck")
      (let [top-card (peek deck)]
        (-> game
            (update-in [player-key :hand] conj top-card)
            (update-in [player-key :deck] pop))))))

(defn play-card [game player-key]
  (let [hand (get-in game [player-key :hand])
        hand-card (peek hand)
        cost (:cost hand-card)
        player-money (get-in game [player-key :money])]
    (cond 
     (empty? hand) (log game "No cards in hand")
     (> cost player-money) (log game "Not enough money to play the card")
     :else (-> game
               (update-in [player-key :hand] pop)
               (update-in [player-key :money] #(- % cost))
               ((:effect hand-card) player-key)))))

(def card-1 {:name "Basic draw card"
             :cost 1
             :effect draw-card})

(def card-2 {:name "Basic money card"
             :cost 3
             :effect (fn [game player-key]
                       (update-in game [player-key :money] #(+ 7 %)))})

