(ns cardgame.core
  (:require [cardgame.cards :as cards]))

(defn build-deck [card-selection]
  (vec (repeatedly 3 (fn [] (rand-nth card-selection)))))

(def all-cards [;cards/card-1
                cards/card-2])

(def deck-1 [cards/card-2 cards/card-1])
(def deck-2 [cards/card-1 cards/card-2])

(defn create-player [player-name deck]
  {:name player-name
   :money 5
   :hand []
   :deck deck
   :in-play []
   :graveyard []})

(def p1 (create-player "Erik" deck-1))
(def p2 (create-player "Marie" deck-2))

(defn new-game [p1 p2]
  {:p1 p1
   :p2 p2
   :messages []})

(defn print-player-state [player]
  (println (str (:name player) ", $" (:money player) ", " (count (:hand player)) 
                " cards in hand, " (count (:deck player)) " cards in deck.")))

(defn print-game-state [game]
  (print-player-state (:p1 game))
  (print-player-state (:p2 game))
  (doall (map println (:messages game)))
  game)

(println "New game!")

(-> (new-game p1 p2)
    (cards/draw-card :p1)
    (cards/play-card :p1)
    print-game-state)

;(clojure.pprint/pprint {:f 500})