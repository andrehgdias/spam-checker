package com.tuta;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedHashSet;
import java.util.Scanner;

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
            System.out.println(
                    "\nSimilarity threshold (If similarity is higher than X it may be considered SPAM - Value recommended: 0.32):");
            float similarityThreshold = scanner.nextFloat();

            System.out.println("\nNumber of similar emails allowed:");
            int numOfSimilarEmailsAllowed = scanner.nextInt();

            Gson gson = new Gson();
            Email[] emails = gson.fromJson(reader, Email[].class);

            LinkedHashSet<String> allWords = new LinkedHashSet<>();
            HashMap<String, Float>[] TF_IDF_DATABASE = new HashMap[emails.length];
            float[][] emailsSimilarity = new float[emails.length][emails.length];

            // Process each object in the array calculating it spam probability
            for (int currEmailIndex = 0; currEmailIndex < emails.length; currEmailIndex++) {
                final Email email = emails[currEmailIndex];
                System.out.println("\n--> Processing email");
                email.print();

                TF_IDF_DATABASE[currEmailIndex] = new HashMap<>();

                String[] words = TextUtils.getCleanWords(email.getBody());
                for (String word : words) {
                    float result = SimilarityUtils.calcTFIDF(emails, email, word);
                    TF_IDF_DATABASE[currEmailIndex].put(word, result);
                }
                allWords.addAll(Arrays.asList(words));
            }

            System.out.println("\n--> Number of unique words: " + allWords.size());
            for (String word : allWords) {
                System.out.println(word);
            }
            System.out.println("------------------------------\n");

            // Cossine Similarity
            for (int currEmailIndex = 0; currEmailIndex < emails.length; currEmailIndex++) {
                System.out.println("\n--> Current email index: " + currEmailIndex);
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
                    emailsSimilarity[currEmailIndex][indexEmailToCompare] = SimilarityUtils.calcCossineSimilarity(
                            currEmailNormalizedVector,
                            emailToCompareNormalizedVector);
                    System.out.println("indexEmailToCompare: " + indexEmailToCompare + " - similarity: "
                            + emailsSimilarity[currEmailIndex][indexEmailToCompare]);
                }
            }

            boolean[] finalEmailSpamClassification = new boolean[emails.length];
            for (int i = 0; i < finalEmailSpamClassification.length; i++) {
                int similarEmailsCount = 0;
                for (int j = 0; j < emailsSimilarity.length; j++) {
                    if (i == j)
                        continue;
                    if (emailsSimilarity[i][j] > similarityThreshold)
                        similarEmailsCount++;
                    if (similarEmailsCount > numOfSimilarEmailsAllowed) {
                        finalEmailSpamClassification[i] = true;
                        break;
                    }
                }
            }

            System.out.println("\n--> Final result");
            System.out.printf("| %-15s | %-4s |\n", "Email Index", "Spam");
            System.out.println("|-----------------|------|");
            for (int i = 0; i < finalEmailSpamClassification.length; i++) {
                System.out.printf("| %-15d | %-4s |\n", i, finalEmailSpamClassification[i] ? "Yes" : "No");
            }

        } catch (InputMismatchException e) {
            System.err.println("Sorry, input wrong format: " + e.toString());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + filePath);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.toString());
        } finally {
            scanner.close();
        }
    }
}
