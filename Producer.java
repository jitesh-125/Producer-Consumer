package com.shubham.producer;

import com.shubham.messageQueue.Message;
import com.shubham.messageQueue.MessageQueue;

import java.util.List;

public class Producer implements Runnable {
    private final MessageQueue messageQueue;
    private final List<Message> messages;

    public Producer(MessageQueue messageQueue, List<Message> messages) {
        this.messageQueue = messageQueue;
        this.messages = messages;
    }

    @Override
    public void run() {
        for(Message message : messages) {
            try {
                messageQueue.produce(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

