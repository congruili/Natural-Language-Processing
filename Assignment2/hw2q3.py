__author__ = 'congrui_li'

import gensim, logging

def main():
    # model = gensim.models.Word2Vec.load('mymodel')
    # sentences = gensim.models.word2vec.Text8Corpus('text8', max_sentence_length=10000)
    # model = gensim.models.Word2Vec(sentences, min_count=10, size=200, workers=4)
    # model.save('mymodel')

    model = gensim.models.Word2Vec.load_word2vec_format('GoogleNews-vectors-negative300.bin', binary=True)
    
    file = open('test.txt', 'r')    
    lines = file.readlines()
    file.close()

    for x in range(len(lines)):
        lines[x] = lines[x].rstrip('\n').split(':')    

    prev = []
    for x in range(len(lines)):
        curt = lines[x]
        if (x + 1) % 4 == 1:
            prev = curt
            print('\nquestion %s:' % (str)((x + 1) / 4 + 1))
            print(prev)
        else: 
            print('n_similarity between (' + prev[0] + ', ' + prev[1] + ') and (' + curt[0] + ', ' + curt[1] + '): ')
            print(model.n_similarity(prev, curt))   

if __name__ == "__main__":main()
