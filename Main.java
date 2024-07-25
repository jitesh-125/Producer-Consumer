package com.shubham;

import com.shubham.consumer.Consumer;
import com.shubham.messageQueue.Message;
import com.shubham.messageQueue.MessageQueue;
import com.shubham.producer.Producer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(3);
        // We are using AtomicInteger for the thread safety, In case we create multiple consumer threads
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();

        Thread producerThread = new Thread(new Producer(messageQueue,
                List.of(new Message("201"),
                        new Message("200"),
                        new Message("201"),
                        new Message("204"),
                        new Message("203"),
                        new Message("400"),
                        new Message("201"),
                        new Message("200"),
                        new Message("400"),
                        new Message("200"),
                        new Message("404"),
                        new Message("TERMINATE"))));

        Thread consumerThread = new Thread(new Consumer(messageQueue,successCount,errorCount));

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Total messages processed successfully: " + successCount.get());
        System.out.println("Total errors encountered: " + errorCount.get());
    }
}
