package com.shubham.consumer;

import com.shubham.messageQueue.Message;
import com.shubham.messageQueue.MessageQueue;

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    private final MessageQueue messageQueue;
    private final AtomicInteger successCount;
    private final AtomicInteger errorCount;

    public Consumer(MessageQueue messageQueue, AtomicInteger successCount, AtomicInteger errorCount) {
        this.messageQueue = messageQueue;
        this.successCount = successCount;
        this.errorCount = errorCount;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = messageQueue.consume();
                // if we receive terminate status break out of the while loop
                if (message.getStatusCode().equals("TERMINATE")) {
                    break;
                }
                processMessage(message);
                successCount.incrementAndGet();
            } catch (Exception e) {
                errorCount.incrementAndGet();
            }
        }
    }

    private void processMessage(Message message) {
        int statusCode = Integer.parseInt(message.getStatusCode());

        // Simulation of error while consuming
        if (statusCode >= 400 && statusCode <= 599) {
            throw new RuntimeException("Failed to process message: " + message);
        }

        System.out.println("Message processed successfully: " + message);
    }

}
