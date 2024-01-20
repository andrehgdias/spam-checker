package com.tuta;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimilarityUtils {
    public static float calcTFIDF(Email[] allEmails, Email currEmail, String word) {
        float numOfWords = TextUtils.countNumOfWords(currEmail.getBody());
        float tf = TextUtils.countWordOccurrence(word, currEmail.getBody()) / numOfWords;
        float idf = 1 + (float) Math.log10((float) (allEmails.length / TextUtils.countDocsHasWord(word, allEmails)));
        return tf * idf;
    }

    public static float[] normalize(HashMap<String, Float> hashMap, LinkedHashSet<String> allWords) {
    public static float[] normalize(Map<String, Float> hashMap, Set<String> allWords) {
        float[] normalizedVector = new float[allWords.size()];
        int wordIndex = 0;
        for (String word : allWords) {
            float TF_IDF = hashMap.getOrDefault(word, 0f);
            normalizedVector[wordIndex++] = TF_IDF;
        }
        return normalizedVector;
    }

    public static float calcCossineSimilarity(float[] currEmailNormalizedVector,
    public static float calcCosineSimilarity(float[] currEmailNormalizedVector,
            float[] emailToCompareNormalizedVector) {
        float dotProduct = 0f;
        double normA = 0;
        double normB = 0;
        for (int i = 0; i < currEmailNormalizedVector.length; i++) {
            dotProduct += currEmailNormalizedVector[i] * emailToCompareNormalizedVector[i];
            normA += Math.pow(currEmailNormalizedVector[i], 2);
            normB += Math.pow(emailToCompareNormalizedVector[i], 2);
        }
        float eucledianDist = (float) (Math.sqrt(normA) * Math.sqrt(normB));
        return dotProduct / eucledianDist;
        float euclideanDist = (float) (Math.sqrt(normA) * Math.sqrt(normB));
        return dotProduct / euclideanDist;
    }

    public static float[] getVector(Map<String, Map<Integer, Float>> tfIdfDatabase, int currEmailIndex) {
        Map<String, Float> vector = new HashMap<>();
        for (String word : tfIdfDatabase.keySet()) {
            vector.put(word, tfIdfDatabase.get(word).getOrDefault(currEmailIndex, 0f));
        }
        return normalize(vector, tfIdfDatabase.keySet());
    }
}
