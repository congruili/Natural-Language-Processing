##**Usage:**

`HMM` is the main class. After compiling `.java` files, execute the `HMM.class` file with the following arguments:

```$ java HMM [name of the training set file] [name of the test set file]```

Sample output for running the files in the **```/wsj_pos```** folder:
```
Baseline approach: 
(for each token, word/true tag/predicted tag)
Vinken/NNP/NNP said/VBD/VBD that/WDT/IN made/VBD/VBD Kent/NNP/NNP exposed/VBN/VBN to/TO/TO cigarette/NN/NN about/RB/RB one/CD/CD year/NN/NN ago/IN/IN ././. 
Accuracy: 0.9230769230769231


HMM approach: 
(for each token, word/true tag/predicted tag)
Vinken/NNP/NNP said/VBD/VBD that/WDT/WDT made/VBD/VBD Kent/NNP/NNP exposed/VBN/VBN to/TO/TO cigarette/NN/NN about/RB/RB one/CD/CD year/NN/NN ago/IN/IN ././. 
Accuracy: 1.0
```


##**My approach for corner cases:**

**Baseline:**

If there is a tie for the tag of a token, choose the tag which appears more frequently in the training set. 
For example, fish appears 4 times as noun and 4 times as verb in the training set. However, noun appears 100 times and verb appears 50 times in the training set. Therefore, in the test set, fish should be tagged as noun.

**HMM:**

If a token in the test set does not exist in the training set, try all existing transition probabilities from the previous token, and set the emission probabilities of this token to 1. (This case does not appear in the sample test set, but this is implemented in the code.)

If the possible transition probabilities from the previous token to the current token all equal to zero, start over from the current token, set the value of each tag as the probability of appearance of the tag in the training set, then continue the normal HMM calculation process after the current token. 


