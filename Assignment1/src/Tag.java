import java.util.HashMap;

class Tag {
    private String name;
    private int nextTagCount, wordCount;
    HashMap<String, Integer> nextTags, words;
    HashMap<String, Double> transProb, emiProb;

    public String getName() {
        return name;
    }

    public int getWordCount() {
        return wordCount;
    }

    public Tag(String name) {
        this.name = name;
        nextTagCount = 0;
        wordCount = 0;
        nextTags = new HashMap<>();
        words = new HashMap<>();
        transProb = new HashMap<>();
        emiProb = new HashMap<>();
    }

    public void updateNextTags(String tagName) {
        if (!nextTags.containsKey(tagName)) nextTags.put(tagName, 0);
        nextTags.put(tagName, nextTags.get(tagName) + 1);
        nextTagCount++;
    }

    public void updateWords(String curtWord) {
        if (!words.containsKey(curtWord)) words.put(curtWord, 0);
        words.put(curtWord, words.get(curtWord) + 1);
        wordCount++;
    }

    public void updateTransProb() {
        double sum = (double) nextTagCount;
        for (String tagName: nextTags.keySet()) {
            transProb.put(tagName, nextTags.get(tagName)/sum);
        }
    }

    public void updateEmiProb() {
        double sum = (double) wordCount;
        for (String curtWord: words.keySet()) {
            emiProb.put(curtWord, words.get(curtWord)/sum);
        }
    }

}
