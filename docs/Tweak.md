Jacob Opdahl & Peter Hanson

CSci 4553 - Hill-Climb Exercise

## Tweak

### The Algorithm:

Our tweak algorithm operates as follows:
- Name: remove-then-random-replace
- Args: answer (with or without a score)
- Returns: answer (without a score)
- Process:
  - If the answer has a weight that is over capacity, remove a random item. Repeat until the answer is no longer over the capacity.
  - When the answer is at an acceptable weight, remove a random item and add a random item back.
  
Other Notes:
- We set it up so that our random add and random remove functions are guaranteed to add or remove an item respectively. That is, remove won't pick a choice that is already a 0 and make it a 0.
  - Upon thinking about this later, this does technically mean our algorithm could get into an infinite loop. This is *highly* unlikely. It would occur if it tries to remove an item and none are in the bag. Given that we are using complete randomness for our starting answer and the smallest amount of choices is 20, this should not happen.
- By an answer, we mean the same general structure that we used in class when creating the random solution algorithm.
- This algorithm can return answers that are overweight. As a result, it is necessary to score answers when comparing in the overall hill-climbing algorithm.
  - We modified the scoring function from class to return the negative of the actual value if an answer is overweight rather than 0.
- Our tweak works entirely by modifying the list of choices. As such, we wrote a helper function to rebuild an answer for the given instance with a new set of choices.
  
### The Why:

Our hill-climbing algorithm is fairly simple. One reason for this choice is Jacob is entirely new to Clojure (and hasn't touched Racket in a long time)
and Peter has limited experience with it as well. Basically, we went along with what Nic said about having
simple working is better than having perfection broken (or something along those lines).

We designed the algorithm to build off directly from where we left off with our random-answer algorithm.
That is, we stuck with a completely random answer as our initial answer. We debated modifying random-answer since answers seem
to frequently be overweight, but in the end, we decided to handle that through the hill-climbing rather than by modifying the
random algorithm. Thus, we have the reason for us deciding to check for answers being overweight in the tweak function; we 
wanted to get answers to a reasonable level (a weight that is close to max but passes). Once answers are there, items can be randomly removed and added to see if a hill can be climbed to a better random answer. We felt this simple tweak, when used
within an overall hill-climb algorithm, should move solutions to local optima at the very least.

### The Future:

Here are a few things that could be done in the future to improve on our inital hill-climb attempt:
- Random restarts are a must! Without them, it is clear our algorithm will get stuck at local optima. This can be seen by running the algorithm (the overall hill-climbing algorithm using the tweak function) repeatedly and watching the the final score shift around to a few distinct answers.
- While *very* unlikely, it would  be good to modify our remove and add choice functions (find-and-remove-choice and find-and-add-choice) such that they never lead to infinite loops. You know, just a good rule-of-thumb.
  - These two functions could also be combined into one as their logic is almost identical. Not sure if it's better practice to have them separate or together in a functional language?
- If random were to output an initial answer that has a weight significantly under the total-capacity (significantly under meaning the swapping of single items will not get the answer close to the capacacity), then our tweak function won't ever climb hills in any significant way. It could have some improvements, but it can't actually climb upward. In other words, the current algorithm is good for going down from being overweight and then hovering about the max capacity. However, it would not work to start way below the capacity and work upward. This may need to be changed (or, it could be totally alright assuming random really will always be overweight).
  - An alternative to modifying tweak would be to go back to random and make it more likely that random answers will be overweight by putting in a higher proportion of 1s into choices.

## Hill-Climbing

### Functions Involved:
- find-and-remove-choice: Removes a random element from a list of choices. (Changes a random 1 to a 0)
  - Args: Choices (as a list)
  - Returns: Choices (as a list)
- find-and-add-choice: Adds a random element to a list of choices. (Changes a random 0 to a 1)
  - Args: Choices (as a list)
  - Returns: Choices (as a list)
- reconstruct-answer: Constructs a new answer for an instance and list of choices.
  - Args: instance, choices
  - Returns: answer (no score)
- remove-then-random-replace: The tweak function. See above.
- hill-climb-racing: Our hill-climb algorithm. Tweaks a random answer a specified number of times and returns the best result out of all the tweak attempts.
  - Args: instance, tweak-function, tweak-times
  - Returns: answer (with score)

### The Results:

We compared the performance of our hill-climbing algorithm to the random-search algorithm made in class. Hill-climb did 10,000 tweaks, and random-search did 10,000 attempts. The results are as follows:
- Problem:
  - hill-climb:
  - random-search:

