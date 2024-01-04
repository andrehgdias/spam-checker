package com.tuta;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class SimilarityUtils {
    public static float calcTFIDF(Email[] allEmails, Email currEmail, String word) {
        float numOfWords = TextUtils.countNumOfWords(currEmail.getBody());
        float tf = TextUtils.countWordOccurance(word, currEmail.getBody()) / numOfWords;
        float idf = 1 + (float) Math.log10(allEmails.length / TextUtils.countDocsHasWord(word, allEmails));
        return tf * idf;
    }

    public static float[] normalize(HashMap<String, Float> hashMap, LinkedHashSet<String> allWords) {
        float[] normalizedVector = new float[allWords.size()];
        int wordIndex = 0;
        for (String word : allWords) {
            float TF_IDF = hashMap.getOrDefault(word, 0f);
            normalizedVector[wordIndex++] = TF_IDF;
        }
        return normalizedVector;
    }

    public static float[] calcCossineSimilarity(float[] currEmailNormalizedVector,
            float[] emailToCompareNormalizedVector) {
        return null;
    }
}
