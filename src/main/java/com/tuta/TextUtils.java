package com.tuta;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class providing various text processing methods.
 */
public class TextUtils {

    /**
     * Removes any trailing punctuation from the end of a given word in the input text.
     * If the word ends with punctuation, it is stripped off.
     *
     * @param input The input text containing the word.
     * @return The word without any punctuation at its end.
     */
    public static String removePunctuationAtWordEnd(String input) {
        if (input == null || input.isEmpty()) return input;

        String regex = "\\p{Punct}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.replaceAll("");
    }

    /**
     * Cleans and retrieves an array of words from the provided text.
     *
     * @param textBody The text body to extract words from.
     * @return An array of cleaned words.
     */
    public static String[] getCleanWords(String textBody) {
        ArrayList<String> words = new ArrayList<>();
        for (String word : textBody.split("\\s+")) {
            String cleanWord = removePunctuationAtWordEnd(word.toLowerCase());
            words.add(cleanWord);
        }
        return words.toArray(new String[0]);
    }

    /**
     * Counts the occurrences of a specific word in the given text.
     *
     * @param word     The word to count.
     * @param textBody The text body to search for occurrences.
     * @return The number of occurrences of the specified word.
     */
    public static int countWordOccurrence(String word, String textBody) {
        int count = 0;
        for (String currWord : textBody.split("\\s+")) {
            String cleanCurrWord = removePunctuationAtWordEnd(currWord.toLowerCase());
            count += cleanCurrWord.equals(word) ? 1 : 0;
        }
        return count;
    }

    /**
     * Counts the number of documents (emails) containing a specific word.
     *
     * @param word      The word to search for in the emails.
     * @param allEmails An array of emails to search through.
     * @return The number of documents containing the specified word.
     */
    public static int countDocsHasWord(String word, Email[] allEmails) {
        int count = 0;
        for (Email email : allEmails) {
            String textBody = email.getBody();
            for (String currWord : textBody.split("\\s+")) {
                String cleanCurrWord = removePunctuationAtWordEnd(currWord.toLowerCase());
                if (cleanCurrWord.equals(word)) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    /**
     * Counts the number of words in the given text.
     *
     * @param textBody The text body to count words in.
     * @return The number of words in the text.
     */
    public static int countNumOfWords(String textBody) {
        String[] words = textBody.split("\\s+");
        return words.length;
    }
}
