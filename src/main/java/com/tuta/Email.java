package com.tuta;

/**
 * Represents an email with a receiver and body.
 */
public class Email {
    private String receiver;
    private String body;

    /**
     * Constructs an email with the specified receiver and body.
     *
     * @param receiver The email receiver.
     * @param body     The email body.
     */
    Email(String receiver, String body) {
        this.receiver = receiver;
        this.body = body;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Returns a string representation of the email in the format:
     * "To: [receiver]\nBody: [body]"
     *
     * @return String representation of the email.
     */
    @Override
    public String toString() {
        return String.format("To: %s\nBody: %s\n", this.receiver, this.body);
    }

    /**
     * Prints the email information to the console using {@code System.out.println}.
     */
    public void print() {
        System.out.println(this);
    }
}