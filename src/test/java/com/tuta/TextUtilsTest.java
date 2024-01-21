package com.tuta;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class TextUtilsTest {

    @Test
    void removePunctuationAtWordEnd() {
        String input = "Best email service: end-to-end encrypted and no ads.";
        String result = TextUtils.removePunctuationAtWordEnd(input);
        assertThat(result).isEqualTo("Best email service: end-to-end encrypted and no ads");
    }

    @Test
    void getCleanWords() {
        String textBody = "Best email service: end-to-end, encrypted and no ads.";
        String[] result = TextUtils.getCleanWords(textBody);
        assertThat(result).containsExactly("best", "email", "service", "end-to-end", "encrypted", "and", "no", "ads");
    }

    @ParameterizedTest
    @CsvSource({"none, 0", "best, 1", "encrypted, 2"})
    void countWordOccurrence(String word, int count) {
        String textBody = "Best email service: end-to-end encrypted and no ads. Fully encrypted and secure!";
        int result = TextUtils.countWordOccurrence(word, textBody);
        assertThat(result).as("Counting word: %s", word).isEqualTo(count);
    }

    @ParameterizedTest
    @CsvSource({"none, 0", "an, 2", "email, 3"})
    void countDocsHasWord(String word, int count) {
        Email[] allEmails = {
                new Email("John", "This is an email."),
                new Email("Jane", "Another email."),
                new Email("Jack", "Not an email.")
        };
        int result = TextUtils.countDocsHasWord(word, allEmails);
        assertThat(result).as("Counting word: %s", word).isEqualTo(count);
    }

    @Test
    void countNumOfWords() {
        String textBody = "Best email service: end-to-end encrypted and no ads.";
        int result = TextUtils.countNumOfWords(textBody);
        assertThat(result).isEqualTo(8);
    }
}