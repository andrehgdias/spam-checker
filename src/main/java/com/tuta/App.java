package com.tuta;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import com.google.gson.Gson;

/**
 * Main class for the Email Processor application.
 * This program analyzes email content, calculates similarity metrics, and classifies emails as spam or non-spam.
 */
public class App {
    /**
     * The entry point of the Email Processor application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
        System.out.println("Enter file path to json:");
        String filePath = scanner.nextLine();

        try (Reader reader = new FileReader(filePath)) {
            // User input for similarity threshold and the number of similar emails allowed
            System.out.println(
                    "\nSimilarity threshold (If similarity is higher than X it may be considered SPAM - Value recommended: 0.32):");
            float similarityThreshold = scanner.nextFloat();

            System.out.println("\nNumber of similar emails allowed:");
            int numOfSimilarEmailsAllowed = scanner.nextInt();

            Gson gson = new Gson();
            Email[] emails = gson.fromJson(reader, Email[].class);

            // TF-IDF Database and Similarity Matrix initialization
            Map<String, Map<Integer, Float>>TF_IDF_DATABASE = new HashMap<>();
            SymmetricMatrix emailsSimilarity = new SymmetricMatrix(emails.length);

            // Process each email calculating its TF-IDF values
            for (int currEmailIndex = 0; currEmailIndex < emails.length; currEmailIndex++) {
                final Email email = emails[currEmailIndex];
                System.out.println("\n--> Processing email");
                email.print();

                String[] words = TextUtils.getCleanWords(email.getBody());
                for (String word : words) {
                    TF_IDF_DATABASE.computeIfAbsent(word, s -> new HashMap<Integer, Float>());
                    Map<Integer, Float> currWordMap = TF_IDF_DATABASE.get(word);
                    float result = SimilarityUtils.calcTFIDF(emails, email, word);
                    currWordMap.put(currEmailIndex, result);
                    TF_IDF_DATABASE.put(word, currWordMap);
                }
            }

            System.out.println("\n--> Number of unique words: " + TF_IDF_DATABASE.keySet().size());

            // Cosine Similarity calculation
            for (int currEmailIndex = 0; currEmailIndex < emails.length; currEmailIndex++) {
                System.out.println("\n--> Current email index: " + currEmailIndex);
                for (int indexEmailToCompare = currEmailIndex + 1; indexEmailToCompare < emails.length; indexEmailToCompare++) {
                    float[] currEmailNormalizedVector = SimilarityUtils.getVector(TF_IDF_DATABASE, currEmailIndex);
                    float[] emailToCompareNormalizedVector = SimilarityUtils.getVector(TF_IDF_DATABASE, indexEmailToCompare);

                    if(Float.isNaN(emailsSimilarity.get(currEmailIndex, indexEmailToCompare))) {
                        float similarity = SimilarityUtils.calcCosineSimilarity(
                                currEmailNormalizedVector,
                                emailToCompareNormalizedVector
                        );
                        emailsSimilarity.set(currEmailIndex, indexEmailToCompare, similarity);
                    System.out.println("indexEmailToCompare: " + indexEmailToCompare + " - similarity: "
                            + emailsSimilarity.get(currEmailIndex, indexEmailToCompare));
                    }
                }
            }

            // Final Email Classification
            System.out.println("\n--> Final classification");
            boolean[] finalEmailSpamClassification = new boolean[emails.length];
            for (int i = 0; i < finalEmailSpamClassification.length; i++) {
                int similarEmailsCount = 0;
                for (int j = 0; j < emails.length; j++) {
                    if (i == j) continue;
                    if (emailsSimilarity.get(i, j) > similarityThreshold) {
                        similarEmailsCount++;
                        System.out.printf("--> Email %d is similar to %d%n", i, j);
                    }
                    if (similarEmailsCount > numOfSimilarEmailsAllowed) {
                        finalEmailSpamClassification[i] = true;
                        System.out.printf("--> Email %d tagged as SPAM%n", i);
                        break;
                    }
                }
                System.out.println();
            }

            System.out.println("\n--> Final result");
            System.out.printf("| %-15s | %-4s |\n", "Email Index", "Spam");
            System.out.println("|-----------------|------|");
            for (int i = 0; i < finalEmailSpamClassification.length; i++) {
                System.out.printf("| %-15d | %-4s |\n", i, finalEmailSpamClassification[i] ? "Yes" : "No");
            }

        } catch (InputMismatchException e) {
            System.err.println("Sorry, input wrong format: " + e);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + filePath);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e);
        } finally {
            scanner.close();
        }
    }
}
