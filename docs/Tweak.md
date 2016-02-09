Jacob Opdahl & Peter Hanson

CSci 4553 - Hill-Climb Exercise

## Tweak

### The Algorithm

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
