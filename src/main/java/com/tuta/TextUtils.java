package com.tuta;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    public static String removePunctuationAtWordEnd(String input) {
        String regex = "\\b\\p{Punct}+(?=(\\s|$))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        String result = matcher.replaceAll("");
        return result;
    }

    public static String[] getCleanWords(String textBody) {
        ArrayList<String> words = new ArrayList<>();
        for (String word : textBody.split("\\s+")) {
            String cleanWord = removePunctuationAtWordEnd(word.toLowerCase());
            words.add(cleanWord);
        }
        return words.toArray(new String[0]);
    }

    public static int countWordOccurance(String word, String textBody) {
        int count = 0;
        for (String currWord : textBody.split("\\s+")) {
            String cleanCurrWord = removePunctuationAtWordEnd(currWord.toLowerCase());
            count += cleanCurrWord.equals(word) ? 1 : 0;
        }
        return count;
    }

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

    public static int countNumOfWords(String textBody) {
        String[] words = textBody.split("\\s+");
        return words.length;
    }
}
