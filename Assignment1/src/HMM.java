import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HMM {

    public static void main(String[] args) {
        File f1 = new File("training.txt");
        Tag start = new Tag("START");
        Tag end = new Tag("END");
        HashMap<String, Tag> tagMap = new HashMap<>();
        tagMap.put("START", start);
        HashMap<String, List<String>> wordMap = new HashMap<>();

        try (BufferedReader br1 = new BufferedReader(new FileReader(f1))) {
            String line;
            while ((line = br1.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0) {
                    String[] tokens = line.split(" +");
                    Tag prev = start;
                    for (int i = 0; i < tokens.length; ++i) {
                        String[] curt = tokens[i].split("/");
                        //System.out.println(curt[0] + " " + curt[1]);
                        String curtWord = curt[0];
                        String tagName = curt[1];

                        if (!wordMap.containsKey(curtWord)) wordMap.put(curtWord, new ArrayList<String>());
                        wordMap.get(curtWord).add(tagName);

                        prev.updateNextTags(tagName);
                        if (!tagMap.containsKey(tagName)) tagMap.put(tagName, new Tag(tagName));
                        tagMap.get(tagName).updateWords(curtWord);
                        prev = tagMap.get(tagName);
                        if (i == tokens.length - 1) prev.updateNextTags("END");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        start.updateTransProb();
        for (String tag: tagMap.keySet()) {
            tagMap.get(tag).updateTransProb();
            tagMap.get(tag).updateEmiProb();
        }

//        System.out.println("Transition Probablity: ");
//        for (String tag: tagMap.keySet()) {
//            System.out.println("tag: " + tag);
//            double sum = 0;
//            for (Map.Entry<String, Double> entry : tagMap.get(tag).transProb.entrySet()) {
//                System.out.println(entry.getKey() + " " + entry.getValue());
//                sum += entry.getValue();
//            }
//            System.out.println("total probablity: " + sum);
//            System.out.print("\n");
//        }

//        System.out.println("Emission Probablity: ");
//        for (String tag: tagMap.keySet()) {
//            System.out.println("tag: " + tag);
//            double sum = 0;
//            for (Map.Entry<String, Double> entry : tagMap.get(tag).emiProb.entrySet()) {
//                System.out.println(entry.getKey() + " " + entry.getValue());
//                sum += entry.getValue();
//            }
//            System.out.println("total probablity: " + sum);
//            System.out.print("\n");
//        }

//        System.out.println("Word Tag Probablity: ");
//        for(String word: wordMap.keySet()) {
//            countTagForWord(word, wordMap.get(word));
//        }

        System.out.println("Baseline approach: ");
        System.out.println("(for each token, the word followed by the true tag, then followed by the predicted tag)");
        File f2 = new File("test.txt");
        int correct = 0, totalTokens = 0;
        try (BufferedReader br2 = new BufferedReader(new FileReader(f2))) {
            String line;
            while ((line = br2.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0) {
                    String[] tokens = line.split(" +");
                    for (int i = 0; i < tokens.length; ++i) {
                        String[] curt = tokens[i].split("/");
                        String curtWord = curt[0];
                        String tagName = curt[1];
                        String trainTag = "";
                        if (!wordMap.containsKey(curtWord)) trainTag = "NN";
                        else {
                            List<String> trainTagList = getTrainTagList(curtWord, wordMap.get(curtWord));
                            if (trainTagList.size() == 1) trainTag = trainTagList.get(0);
                            else trainTag = filterTrainTagList(trainTagList, tagMap);
                        }

                        if (trainTag.equals(tagName)) correct++;
                        totalTokens++;
                        System.out.print(tokens[i] + "/" + trainTag + " ");
                    }
                }
                System.out.println();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println(String.format("Accuracy: %s", (double)correct/totalTokens));


        System.out.println("\n\nHMM approach: ");
        System.out.println("(for each token, the word followed by the true tag, then followed by the predicted tag)");
        File f3 = new File("test.txt");
        correct = 0;
        totalTokens = 0;
        int totalRows = 0;
        List<String> tagNames = new ArrayList<>();
        HashMap<String, Integer> rowNameToNo = new HashMap<>();
        HashMap<Integer, String> rowNoToName = new HashMap<>();
        tagNames.add("START");
        rowNameToNo.put("START", 0);
        rowNoToName.put(0, "START");

        for (String tag: tagMap.keySet()) {
            totalRows++;
            tagNames.add(tag);
            rowNameToNo.put(tag, totalRows);
            rowNoToName.put(totalRows, tag);
        }
        totalRows++;

        try (BufferedReader br3 = new BufferedReader(new FileReader(f3))) {
            String line;
            while ((line = br3.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0) {
                    String[] tokens = line.split(" +");
                    double[][] probs = new double[totalRows][tokens.length + 1];
                    probs[0][0] = 1.0;
                    for (int i = 0; i < tokens.length; ++i) {
                        String[] curt = tokens[i].split("/");
                        String curtWord = curt[0];
                        String tagName = curt[1];
                        String trainTag = "";

                        for (int j = 0; j < totalRows; ++j) {
                            if (probs[j][i] != 0) {
                                Tag prevTag = tagMap.get(rowNoToName.get(j));
                                for (String nextTag: prevTag.nextTags.keySet()) {
                                    double transProb = prevTag.transProb.get(nextTag);
                                    double emiProb;
                                    if (!wordMap.containsKey(curtWord)) emiProb = 1.0;
                                    else if (tagMap.get(nextTag).words.containsKey(curtWord)) {
                                        emiProb = tagMap.get(nextTag).emiProb.get(curtWord);
                                    } else emiProb = 0.0;

                                    int curtRow = rowNameToNo.get(nextTag);
                                    probs[curtRow][i + 1] = Math.max(probs[curtRow][i + 1], probs[j][i] * transProb * emiProb);
                                }
                            }
                        }

                        int maxIndex = 0;
                        for (int k = 1; k < totalRows; ++k) {
                            if (probs[k][i + 1] > probs[maxIndex][i + 1]){
                                maxIndex = k;
                            }
                        }

                        trainTag = rowNoToName.get(maxIndex);

                        if (maxIndex == 0) {
                            List<String> wordTags = wordMap.get(curtWord);
                            HashMap<String, Double> map = new HashMap<>();
                            double sum = 0.0;
                            for (String tag: wordTags) {
                                if (!map.containsKey(tag)) map.put(tag, 0.0);
                                map.put(tag, map.get(tag) + 1);
                                sum = sum + 1.0;
                            }

                            double max = 0.0;
                            for (String tag: map.keySet()) {
                                map.put(tag, map.get(tag) / sum);
                                probs[rowNameToNo.get(tag)][i + 1] = map.get(tag);
                                max = Math.max(map.get(tag), max);
                            }

                            List<String> trainTagList = new ArrayList<>();

                            for (String tag: map.keySet()) {
                                if (map.get(tag) == max) trainTagList.add(tag);
                            }

                            if (trainTagList.size() == 1) trainTag = trainTagList.get(0);
                            else trainTag = filterTrainTagList(trainTagList, tagMap);
                        }

                        if (trainTag.equals(tagName)) correct++;
                        totalTokens++;
                        System.out.print(tokens[i] + "/" + trainTag + " ");
                    }

                    //print probs matrix
//                    System.out.println();
//                    for (int i = 0; i < probs.length; ++i) {
//                        for (int j = 0; j < probs[0].length; ++j) {
//                            System.out.print(probs[i][j] + " ");
//                        }
//                        System.out.println();
//                    }
                }
                System.out.println();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println(String.format("Accuracy: %s", (double)correct/totalTokens));

    }

    public static List<String> getTrainTagList(String word, List<String> wordTags) {
        HashMap<String, Integer> map = new HashMap<>();
//        int sum = 0;
        for (String tag: wordTags) {
            if (!map.containsKey(tag)) map.put(tag, 0);
            map.put(tag, map.get(tag) + 1);
//            ++sum;
        }

        int max = 0;
        for (String tag: map.keySet()) {
            max = Math.max(map.get(tag), max);
        }

        List<String> rst = new ArrayList<>();

        for (String tag: map.keySet()) {
            if (map.get(tag) == max) rst.add(tag);
        }

        return rst;

//        for (String tag: map.keySet()) map.put(tag, map.get(tag)/sum);
//
//        System.out.println("word: " + word);
//        double total = 0;
//        for (Map.Entry<String, Double> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//            total += entry.getValue();
//        }
//        System.out.println("total probablity: " + total);
//        System.out.print("\n");
    }

    public static String filterTrainTagList(List<String>trainTagList, HashMap<String, Tag> tagMap) {
        String rst = "NN";
        int max = 0;
        for (String tag: trainTagList) {
            int curt = tagMap.get(tag).getWordCount();
            if (curt > max) {
                max = curt;
                rst = tag;
            }
        }

        return rst;
    }

}
