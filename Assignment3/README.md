Original data is in the `English_data` folder.

Features generated for each token:
+ Gazetteers. Value: PERSON; ORG; LOCATION; O (None of the above).
+ Case of first letter. Value: CAPITALIZED; LOWERCASE.

Step by step:

Convert the original training data into the right format for the [SimpleTagger](http://mallet.cs.umass.edu/sequences.php) API, also add features to the training data (each line of `train_features_label` contains a token, features of the token and the true label):<br />
```
$ python data_transform.py train train_nwire train_features_label
```

Convert the original test data into the right format for the SimpleTagger API (each line of `test_true` contains a token and the true label, each line of `test_features` contains a token and features of the token, each line of `test_token` contains only the token): <br />
``` 
$ python data_transform.py test test_nwire test_true test_features test_token
```

Train the CRF model using the training data (documentation of the SimpleTagger API is available [here](http://mallet.cs.umass.edu/api/)):<br />
``` 
$ java –cp "/Users/Baymax/Desktop/NLP/Assignment3/mallet/class:/Users/Baymax/Desktop/NLP/Assignment3/mallet/lib/mallet-deps.jar" cc.mallet.fst.SimpleTagger --train true --model-file crf --threads 8 train_features_label
```

Used the CRF model and the generated features to predict labels for the test data (the predicted labels are saved in a file called `predict_label`):<br />
```
$ java -cp "/Users/Baymax/Desktop/NLP/Assignment3/mallet/class:/Users/Baymax/Desktop/NLP/Assignment3/mallet/lib/mallet-deps.jar" cc.mallet.fst.SimpleTagger --model-file crf test_features > predict_label
```

Bind each token in the test set with the predicted label, results are saved in a file called ` test_predict `:<br />
```
$ python bind_test_token_and_label.py test_token predict_label test_predict
```

cd to the ` scorer ` folder; create two subdirectories under `scorer` called `pred` and `test`; copy   `test_predict` into the `pred` folder, copy `test_true` into the `test` folder, change both of the files to the same name, such as `test` (the output and gold standard should have consistent file name); inside the `scorer` folder run the following command which runs the scorer tool provided:
```
$ java Scorer_Independent pred test
```

Here is the final output:
```
Number of docs: 1
Performance: 
Number of correct: 1567.0
Number of answer: 2113.0
Number of gold: 2754.0
Precision: 0.7415996213913867
Recall: 0.5689905591866377
F_1: 0.643928498048079
Breakdown performance
Type : ORG
Number of correct: 239.0
Number of gold: 660.0
Number of answer: 417.0
Precision: 0.5731414868105515
Recall: 0.3621212121212121
F_1: 0.44382544103992577
Type : PER
Number of correct: 599.0
Number of gold: 1001.0
Number of answer: 859.0
Precision: 0.6973224679860303
Recall: 0.5984015984015985
F_1: 0.6440860215053764
Type : GPE
Number of correct: 729.0
Number of gold: 1093.0
Number of answer: 837.0
Precision: 0.8709677419354839
Recall: 0.666971637694419
F_1: 0.755440414507772
```
5-folder cross validation: divide the training set evenly into 5 parts (be careful not to cut in the middle of a sentence); every time use one part as the test set and use the rest 4 parts as the training set to train the CRF model, test the CRF model on the test set to get the performance summaries; there are totally 5 CRF models generated, the one with the highest F-score is used as the final model to test against the true test data.

The GPE F-score is the highest, then follows the PER, ORG has the lowest F-score. The reason is that for the "Gazetteers" feature used, LOCATION is the easiest category to be identified compared to PERSON and ORG.
