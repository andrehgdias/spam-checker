package com.tuta;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Scanner;

import org.apache.commons.math3.linear.RealVector;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
        System.out.println("Enter file path to json:");
        String filePath = scanner.nextLine();

        try (Reader reader = new FileReader(filePath);) {
            Gson gson = new Gson();
            Email[] emails = gson.fromJson(reader, Email[].class);

            LinkedHashSet<String> allWords = new LinkedHashSet<>();
            HashMap<String, Float>[] TF_IDF_DATABASE = new HashMap[emails.length];
            float[][] emailsSimilarity = new float[emails.length][emails.length];

            // Process each object in the array calculating it spam probability
            for (int currEmailIndex = 0; currEmailIndex < emails.length; currEmailIndex++) {
                final Email email = emails[currEmailIndex];
                System.out.println("--> Processing email");
                email.print();

                TF_IDF_DATABASE[currEmailIndex] = new HashMap<>();

                String[] words = TextUtils.getCleanWords(email.getBody());
                for (String word : words) {
                    float result = SimilarityUtils.calcTFIDF(emails, email, word);
                    TF_IDF_DATABASE[currEmailIndex].put(word, result);
                }
                allWords.addAll(Arrays.asList(words));

                /*
                 * for (String word : TF_IDF_DATABASE[currEmailIndex].keySet()) {
                 * System.out.println(word + ": " + TF_IDF_DATABASE[currEmailIndex].get(word));
                 * }
                 */
            }

            // Cossine Similarity
            for (int currEmailIndex = 0; currEmailIndex < emails.length; currEmailIndex++) {
                for (int indexEmailToCompare = 0; indexEmailToCompare < emails.length; indexEmailToCompare++) {
                    if (indexEmailToCompare == currEmailIndex) {
                        emailsSimilarity[currEmailIndex][indexEmailToCompare] = 1;
                        continue;
                    }
                    float[] currEmailNormalizedVector = SimilarityUtils.normalize(
                            TF_IDF_DATABASE[currEmailIndex],
                            allWords);
                    float[] emailToCompareNormalizedVector = SimilarityUtils.normalize(
                            TF_IDF_DATABASE[indexEmailToCompare],
                            allWords);
                    emailsSimilarity[currEmailIndex] = SimilarityUtils.calcCossineSimilarity(
                            currEmailNormalizedVector,
                            emailToCompareNormalizedVector);
                }

                float[] similarities = emailsSimilarity[currEmailIndex];
                for (float similarity : similarities) {
                    System.out.println("currEmailIndex: " + currEmailIndex + " - similarity: " + (similarity));
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
