package com.tuta;

public class Email {
    private String receiver;
    private String body;

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

    @Override
    public String toString() {
        return String.format("To: %s\nBody: %s\n", this.receiver, this.body);
    }

    public void print() {
        System.out.println(this);
    }
}