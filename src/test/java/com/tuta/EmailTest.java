package com.tuta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;


class EmailTest {
    final String initialReceiver = "andredias@tuta.io";
    final String initialBody = "Email full body.\nNew paragraph.";
    Email email;

    @BeforeEach
    void setup(){
        email = new Email(initialReceiver, initialBody);
    }

    @Test
    void getReceiver() {
        assertThat(email.getReceiver()).isEqualTo(initialReceiver);
    }

    @Test
    void setReceiver() {
        final String newReceiver = "andredias@tuta.com";
        email.setReceiver(newReceiver);
        assertThat(email.getReceiver()).isEqualTo(newReceiver);
    }

    @Test
    void getBody() {
        assertThat(email.getBody()).isEqualTo(initialBody);
    }

    @Test
    void setBody() {
        final String newBody = "New email full body.\nSecond paragraph.";
        email.setBody(newBody);
        assertThat(email.getBody()).isEqualTo(newBody);
    }

    @Test
    void testToString() {
        String expectedString = String.format("To: %s\nBody: %s", initialReceiver, initialBody);
        assertThat(email.toString()).isEqualTo(expectedString);
    }

    @Test
    void print() {
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        email.print();

        String expectedOutput = email.toString();
        assertThat(outputStream.toString().trim()).isEqualTo(expectedOutput);

        // Reset System.out
        System.setOut(System.out);
    }
}