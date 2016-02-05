(ns simple-search.core
  (:use simple-search.knapsack-examples.knapPI_11_20_1000
        simple-search.knapsack-examples.knapPI_13_20_1000
        simple-search.knapsack-examples.knapPI_16_20_1000))

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

(defn remove-random-choice
  "Removes a random choice from choices in an instance"
  [instance]
  (let [choices ()
        included (included-items (:items instance) choices)]
  {:instance instance
   :choices choices
   :total-weight (reduce + (map :weight included))
   :total-value (reduce + (map :value included))}))

(defn find-and-remove-choice
  "Takes a list of choices and returns the same list with one choice removed randomly."
  [choices]
  (let [sum (reduce + choices)]
    (def newSum sum)
    (while (= newSum sum)
      (let [randLoc #(rand-int (count choices))]
        (def newSum (reduce + (assoc (def choicesVector (into [] choices)) randLoc 0)))
      )
    ))
    choices
    )

(find-and-remove-choice '(0 1 0 0 0 0 1 0 0 0 0 0 1))

;;; It might be cool to write a function that
;;; generates weighted proportions of 0's and 1's.


;;; We modified the score such that answers that are overweight return negative their score.
(defn score
  "Takes the total-weight of the given answer unless it's over capacity,
   in which case we return 0."
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

(time (random-search knapPI_16_20_1000_1 1000000
))

(random-search knapPI_16_20_1000_1 10000)



;-=-=-=-=-=-=-=- Jacob and Peter's Work Starts Here -=-=-=-=-=-=-=-
(score (random-answer knapPI_16_20_1000_1))

(defn remove-then-random-replace
  "Takes an instance. If the instance is over capacity, removes items until it is not. If it is not, removes a random and add a random."
  [instance]
  (if (> (instance :total-weight) (:capacity (:instance instance)))
    (while (> (instance :total-weight) (:capacity (:instance instance)))

      )
    "Underweight"
    )
  )



(remove-then-random-replace (random-answer knapPI_16_20_1000_1))










