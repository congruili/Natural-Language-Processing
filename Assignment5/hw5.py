import json
from sets import Set
from nltk.sentiment.vader import SentimentIntensityAnalyzer
import os
curt_dir = os.getcwd()
target_dir = curt_dir + '/The First Presidential Debate/comments/'

hillary_words = ['hillary', 'clinton', 'she', 'her', 'hers']
# hillary = ['hillary', 'clinton']
donald_words = ['donald', 'trump', 'he', 'his']
# donald = ['donald', 'trump']

hillary_stats = {'pos': 0, 'neg': 0, 'neu': 0}
donald_stats = {'pos': 0, 'neg': 0, 'neu': 0}

def sentiment_analyzer(comment):
    sentences = comment.split('\n')
    sid = SentimentIntensityAnalyzer()
    h_stats = Set()
    d_stats = Set()
    for sentence in sentences:
        ss = sid.polarity_scores(sentence)
        if abs(ss['pos'] - ss['neg']) >= 0.1:
            if ss['pos'] > ss['neg']: curt = 'pos'
            else: curt = 'neg'
        else: curt = 'neu'        

        for word in hillary_words:
            if word in sentence.lower():
                h_stats.add(curt)

        for word in donald_words:
            if word in sentence.lower():
                d_stats.add(curt)

    for token in h_stats:
        hillary_stats[token] += 1

    for token in d_stats:
        donald_stats[token] += 1

count = 0

for i in range(0, 180):
    json_file = target_dir + 'data_' + str(i) + '.json'
    json_data = open(json_file)
    data = json.load(json_data)
    count += len(data["items"])
    for item in data["items"]:
        comment = item["snippet"]["topLevelComment"]["snippet"]["textDisplay"]
        sentiment_analyzer(comment)

print 'total number of comments: ' + str(count)
print '\nSentiment distribution for Hillary Clinton: '
print hillary_stats
print '\nSentiment distribution for Donald Trump: '
print donald_stats




