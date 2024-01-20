package com.tuta;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for calculating similarity metrics between emails based on TF-IDF analysis.
 *
 * <p>
 * This class provides methods to compute various similarity metrics, such as TF-IDF values, cosine similarity,
 * and vector normalization, facilitating the comparison of content between different emails. It leverages TF-IDF
 * databases to represent the importance of words within each email and offers functions to analyze and compare
 * email content for similarity assessments.
 * </p>
 *
 * Usage Example:
 * <pre>{@code
 * // Initialize TF-IDF database and calculate similarity metrics
 * Map<String, Map<Integer, Float>> tfIdfDatabase = initializeTFIDFDatabase(emails);
 * int currentEmailIndex = 0;
 * float[] currentEmailVector = SimilarityUtils.getVector(tfIdfDatabase, currentEmailIndex);
 * float[] nextEmailVector = SimilarityUtils.getVector(tfIdfDatabase, nextEmailVector);
 * float similarityScore = SimilarityUtils.calcCosineSimilarity(currentEmailVector, otherEmailVector);
 * }</pre>
 *
 * <p>
 * Note: Ensure proper initialization of TF-IDF databases before using the similarity calculation methods.
 * </p>
 */
public class SimilarityUtils {

    /**
     * Calculates the Term Frequency-Inverse Document Frequency (TF-IDF) value of a word in the context of a set of emails.
     *
     * <p>
     * The TF-IDF score reflects the importance of a word within a specific email, considering its frequency in that email
     * compared to its rarity across all emails.
     * </p>
     *
     * @param allEmails An array containing all the emails for analysis.
     * @param currEmail The email under evaluation for the TF-IDF score calculation.
     * @param word The word for which the TF-IDF value is determined.
     * @return The TF-IDF value of the specified word within the given email.
     */
    public static float calcTFIDF(Email[] allEmails, Email currEmail, String word) {
        float numOfWords = TextUtils.countNumOfWords(currEmail.getBody());
        float tf = TextUtils.countWordOccurrence(word, currEmail.getBody()) / numOfWords;
        float idf = 1 + (float) Math.log10((float) (allEmails.length / TextUtils.countDocsHasWord(word, allEmails)));
        return tf * idf;
    }

    /**
     * Normalizes the TF-IDF values of words in the given HashMap based on a specified set of words.
     *
     * <p>
     * TF-IDF values for each word are retrieved from the provided HashMap, and a normalized vector is
     * generated based on the specified set of all words. The normalized vector ensures consistent analysis
     * of TF-IDF values for further processing, such as calculating similarity between emails.
     * </p>
     *
     * @param hashMap The HashMap containing word-TF-IDF pairs.
     * @param allWords The set of all words for which TF-IDF values are considered.
     * @return A normalized vector representing TF-IDF values for the specified set of words.
     */
    public static float[] normalize(Map<String, Float> hashMap, Set<String> allWords) {
        float[] normalizedVector = new float[allWords.size()];
        int wordIndex = 0;
        for (String word : allWords) {
            float TF_IDF = hashMap.getOrDefault(word, 0f);
            normalizedVector[wordIndex++] = TF_IDF;
        }
        return normalizedVector;
    }

    /**
     * Calculates the cosine similarity between the normalized vectors of two emails.
     *
     * <p>
     * Cosine similarity is a measure of similarity between two vectors of an inner product space that measures
     * the cosine of the angle between them. In this context, it quantifies the similarity of the content between
     * the current email and another email to be compared.
     * </p>
     *
     * @param currEmailNormalizedVector The normalized vector representation of the current email.
     * @param emailToCompareNormalizedVector The normalized vector representation of the email to be compared.
     * @return The cosine similarity between the two email vectors.
     */
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
        float euclideanDist = (float) (Math.sqrt(normA) * Math.sqrt(normB));
        return dotProduct / euclideanDist;
    }

    /**
     * Retrieves the vector representation of a specified email index from the TF-IDF database.
     *
     * <p>
     * The vector represents the TF-IDF values of each word in the email. The vector is then normalized
     * to ensure consistency in the analysis of email content.
     * </p>
     *
     * @param tfIdfDatabase The TF-IDF database containing word indices and their corresponding TF-IDF values.
     * @param currEmailIndex The index of the email for which the vector representation is requested.
     * @return The normalized vector representation of the specified email.
     */
    public static float[] getVector(Map<String, Map<Integer, Float>> tfIdfDatabase, int currEmailIndex) {
        Map<String, Float> vector = new HashMap<>();
        for (String word : tfIdfDatabase.keySet()) {
            vector.put(word, tfIdfDatabase.get(word).getOrDefault(currEmailIndex, 0f));
        }
        return normalize(vector, tfIdfDatabase.keySet());
    }
}
