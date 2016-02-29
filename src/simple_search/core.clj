(ns simple-search.core
  (:use simple-search.knapsack-examples.knapPI_11_20_1000
        simple-search.knapsack-examples.knapPI_13_20_1000
        simple-search.knapsack-examples.knapPI_16_20_1000
        simple-search.knapsack-examples.knapPI_11_1000_1000
        simple-search.knapsack-examples.knapPI_13_1000_1000
        simple-search.knapsack-examples.knapPI_16_1000_1000))

;;; An answer will be a map with (at least) four entries:
;;;   * :instance
;;;   * :choices - a vector of 0's and 1's indicating whether
;;;        the corresponding item should be included
;;;   * :total-weight - the weight of the chosen items
;;;   * :total-value - the value of the chosen items

(defn included-items
  "Takes a sequences of items and a sequence of choices and
  returns the subsequence of items corresponding to the 1's
  in the choices sequence."
  [items choices]
  (map first
       (filter #(= 1 (second %))
               (map vector items choices))))

(defn random-answer
  "Construct a random answer for the given instance of the
  knapsack problem."
  [instance]
  (let [choices (repeatedly (count (:items instance))
                            #(rand-int 2))
        included (included-items (:items instance) choices)]
    {:instance instance
     :choices choices
     :total-weight (reduce + (map :weight included))
     :total-value (reduce + (map :value included))}))

;;; It might be cool to write a function that
;;; generates weighted proportions of 0's and 1's.


;;; We modified the score such that answers that are overweight return negative their score.
(defn score
  "Takes the total-weight of the given answer unless it's over capacity,
   in which case we return the negative of what the value would be."
  [answer]
  (if (> (:total-weight answer)
         (:capacity (:instance answer)))
    (- 0 (:total-value answer))
    (:total-value answer)))


(defn add-score
  "Computes the score of an answer and inserts a new :score field
   to the given answer, returning the augmented answer."
  [answer]
  (assoc answer :score (score answer)))

(defn random-search
  [instance max-tries]
  (apply max-key :score
         (map add-score
              (repeatedly max-tries #(random-answer instance)))))


;-=-=-=-=-=-=-=- Jacob and Peter's Work Starts Here -=-=-=-=-=-=-=-


(defn find-and-remove-choice
  "Takes a list of choices and returns the same list with one choice removed randomly."
  [choices]
  (def choicesVector (vec choices))
  (loop [rand #(rand-int (count choicesVector))]
    (if (= (get choicesVector rand) 1)
      (reverse (into '() (assoc choicesVector rand 0)))
      (recur (#(rand-int (count choicesVector))))
      )
  ))


;; If time allows, combine this and find-and-remove-choice.
(defn find-and-add-choice
  "Takes a list of choices and returns the same list with one choice added randomly."
  [choices]
  (def choicesVector (vec choices))
  (loop [rand #(rand-int (count choicesVector))]
    (if (= (get choicesVector rand) 0)
      (reverse (into '() (assoc choicesVector rand 1)))
      (recur (#(rand-int (count choicesVector))))
      )
  ))


(defn reconstruct-answer
  "takes an instance and a set list of choices and returns the new answer."
  [instance choices]
  (let [included (included-items (instance :items) choices)]
  {:instance instance
   :choices choices
   :total-weight (reduce + (map :weight included))
   :total-value (reduce + (map :value included))}))


;; This will be our initial tweak function.
(defn remove-then-random-replace
  "Takes an answer. If the answer is over capacity, removes items until it is not. If it is not, removes a random and add a random."
  [answer]
  (if (> (answer :total-weight) (:capacity (:instance answer)))
    (remove-then-random-replace (reconstruct-answer (answer :instance) (find-and-remove-choice (answer :choices))))
    (reconstruct-answer (answer :instance)
                        (find-and-add-choice
                         (find-and-remove-choice (answer :choices))))
    )
  )



; Make a function to return a copy of initial
; Call Tweak on copy of initial
; Score the result
; If the resulting score is better than in the original, make it the new original.
; Proceed to tweak original again.
(defn hill-climb-racing
  "Start with a random answer for an instance, apply tweak-function tweak-times until done."
  [tweak-function instance tweak-times]
  (def initial (add-score (random-answer instance)))
  (loop [times-left tweak-times
         answer initial]
	  (def tweaked (add-score (tweak-function answer)))
	  (if (> (answer :score) (tweaked :score))
		  (if (> times-left 0)
			  (recur (dec times-left) answer)
			  answer
		  )
		  (if (> times-left 0)
			  (recur (dec times-left) tweaked)
			  tweaked
		  )
	  ))
  )


;; To match up with Nic's data-gatherer, modified the function to do 30 random restarts.
(defn hill-random-restarts
  "Make a random answer and tweak it. After so long, randomly generate a
  new solution and tweak it and see if it's better than the best"
  [instance tweak-times]
  (loop [times-left 10
         answer {:score -1}]
	  (def climbed (hill-climb-racing remove-then-random-replace instance tweak-times))
	  (if (> (answer :score) (climbed :score))
		  (if (> times-left 0)
			  (recur (dec times-left) answer)
			  answer
		  )
		  (if (> times-left 0)
			  (recur (dec times-left) climbed)
			  climbed
		  )
	  ))
  )


;~~~~~~~~~~~~~~~~~~~~~~~~ Peter's Work Starts Here ~~~~~~~~~~~~~~~~~~~~~~~~

;~~~~~~~~~~~~~~~~~~ Mutation ~~~~~~~~~~~~~~~~~~

(defn cull-the-weak
  ;picks the best best of the group for breeding, its simple eugenics.
  [best population]
    (if (= (count population) 0)
      best
      (cull-the-weak (if (> (:score best) (:score (first population)))
                   best (first population)) (rest population))))


(defn remove-failures
  [population child-count]
    (take success-rate (sort-by :score > population)))


(defn babby-maker
  [population child-count mutations]
  (flatten (for [x population] (repeatedly child-count #(mutations x)))))





(defn mutate
  "Swaps one bit, changes a random choice."
  [choices]
  (def choicesVector (vec choices))
  (loop [rand #(rand-int (count choicesVector))]

    (if (= (get choicesVector rand) 1)
      ((reverse (into '() (assoc choicesVector rand 0))) ;if the choice is deselected, it selects it.
      (recur (#(rand-int (count choicesVector)))))

      ((reverse (into '() (assoc choicesVector rand 1))) ;if the choice is selected, it deselects it.
      (recur (#(rand-int (count choicesVector)))))
      )
  ))


(defn mutator
  "Takes an answer. If the answer is over capacity, removes items until it is not. If it is not, removes a random and add a random."
  [answer]
  (if (> (answer :total-weight) (:capacity (:instance answer)))
    (mutate (reconstruct-answer (answer :instance) (mutate (answer :choices))))

    (reconstruct-answer (answer :instance)
                        (mutate
                         (mutate (answer :choices))))
    )
  )


(defn mutator
  [choices]
  (def choicesVector (vec choices))
  (loop [rand #(rand-int (count choicesVector))]

    (if (= (get choicesVector rand) 1)



)))

(defn mutate
  [pos choices]
  (










