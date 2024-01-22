package com.tuta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SimilarityUtilsTest {
    private Email[] sampleEmails;
    private Map<String, Map<Integer, Float>> tfIdfDatabase;

    @BeforeEach
    void setUp() {
        // Create sample emails for testing
        sampleEmails = new Email[]{
                new Email("John", "This is email 1."),
                new Email("Jane", "This is email 2."),
                new Email("Jack", "Email 3 has some unique words."),
        };

        // Initialize TF-IDF database
        tfIdfDatabase = new HashMap<>();
        String[] allWords = {"this", "is", "email", "1", "2", "3", "has", "some", "unique", "words"};
        for (String word : allWords) {
            Map<Integer, Float> wordTfIdfMap = new HashMap<>();
            for (int i = 0; i < sampleEmails.length; i++) {
                wordTfIdfMap.put(i, SimilarityUtils.calcTFIDF(sampleEmails, sampleEmails[i], word));
            }
            tfIdfDatabase.put(word, wordTfIdfMap);
        }
    }

    @Test
    void calcTFIDF() {
        float tfidfValue = SimilarityUtils.calcTFIDF(sampleEmails, sampleEmails[0], "email");
        System.out.println(tfidfValue);
        assertThat(tfidfValue).isGreaterThanOrEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource({
            "this, 0.1",
            "email, 0.16",
    })
    void normalize(String word, float value) {
        // Create a sample TF-IDF vector for the word "email"
        Map<String, Float> tfidfVector = new HashMap<>();
        tfidfVector.put(word, value);

        // Normalize the vector
        float[] normalizedVector = SimilarityUtils.normalize(tfidfVector, tfIdfDatabase.keySet());
        assertThat(normalizedVector).containsOnlyOnce(value).containsOnly(value, 0f);
    }

    @Test
    void calcCosineSimilarity() {
        float[] vector1 = SimilarityUtils.getVector(tfIdfDatabase, 0);
        float[] vector2 = SimilarityUtils.getVector(tfIdfDatabase, 1);

        // Assert cosine similarity is between 0 and 1
        float similarity = SimilarityUtils.calcCosineSimilarity(vector1, vector2);
        assertThat(similarity).isBetween(0f, 1f);

        // Assert cosine similarity between identical vectors is exactly 1
        similarity = SimilarityUtils.calcCosineSimilarity(vector1, vector1);
        assertThat(similarity).isEqualTo(1f);

        // Assert cosine similarity between different vectors is exactly 0
        float[] newVector = new float[vector1.length];
        Arrays.fill(newVector, 0f);
        similarity = SimilarityUtils.calcCosineSimilarity(vector1, newVector);
        assertThat(similarity).isEqualTo(0f);
    }

    @Test
    void getVector() {
        // Get a normalized vector for a specific email index
        float[] vector = SimilarityUtils.getVector(tfIdfDatabase, 1);
        assertThat(vector).containsExactly(  0.0f, 0.0f, 0.3692803f, 0.0f, 0.0f, 0.25f, 0.0f, 0.25f, 0.0f, 0.25f);
    }
}