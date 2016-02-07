(+ 1 0)

(defn find-and-remove-choice
  "Takes a list of choices and returns the same list with one choice removed randomly."
  [choices]
  (def choicesVector (vec choices))
  (loop [rand #(rand-int (count choicesVector))]
    (if (= (get choicesVector rand) 1)
      (reverse (into '() (assoc choicesVector rand 0)))
      (recur (#(rand-int (count choicesVector))))
      )
  )
  )

(find-and-remove-choice '(0 1 0 0 0 0 1 0 0 0 0 0 1))
