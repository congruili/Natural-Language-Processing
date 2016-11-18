``nltk.sentiment.vader`` API was used for sentiment analysis. 
* Documentation is available [here]( http://www.nltk.org/howto/sentiment.html). 
* Source code is available [here]( http://www.nltk.org/_modules/nltk/sentiment/vader.html).

std output of ``hw5.py``:

```
total number of comments: 17773

Sentiment distribution for Hillary Clinton: 
{'neg': 2648, 'neu': 4323, 'pos': 2716}

Sentiment distribution for Donald Trump: 
{'neg': 5271, 'neu': 8708, 'pos': 5484}
```
Sentiment analysis was conducted on the sentence level. Each comment may have multiple sentences. One comment could belong to one, both, or neither of the candidates. One comment could have a single sentiment tag or multiple tags for a candidate. Example result for a single sentence:
```
>>> from nltk.sentiment.vader import SentimentIntensityAnalyzer
>>> sid = SentimentIntensityAnalyzer()
>>> sentence = 'We love you Hillary! Make America Greater! \n#Hillary2K16 \ud83c\uddfa\ud83c\uddf8\ufeff'
>>> sid.polarity_scores(sentence)
{'neg': 0.0, 'neu': 0.49, 'pos': 0.51, 'compound': 0.8065}
```
Ignoring the ``'compound'`` score, the sum of ``'pos'``, ``'neg'``, and ``'neu'`` scores always equals to 1.0. The threshold I used is:</br>
If the difference between ``'pos'`` and ``'neg'`` scores is at least 0.1, the sentiment tag for this sentence would be the large one of ``'pos'`` and ``'neg'``. Otherwise, it either means the ``'pos'`` and ``'neg'`` scores are too close or both of them are too small, then the sentiment tag for this sentence would be ``'neu'``.
