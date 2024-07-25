package com.shubham.messageQueue;

public class Message {
    private final String statusCode;

    public Message(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return "Message{" +
                "statusCode='" + statusCode + '\'' +
                '}';
    }
}
