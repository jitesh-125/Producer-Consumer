package com.shubham.messageQueue;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {
    private final Queue<Message> queue;
    private final int queueSize;

    public MessageQueue(int queueSize) {
        queue = new LinkedList<>();
        this.queueSize = queueSize;
    }

    // The thread which access this method will acquire a monitor lock as it is a synchronized method
    public synchronized void produce(Message message) throws Exception {
        while (queue.size() == queueSize) {
            System.out.println("Queue is full, Producer is waiting for Consumer");
            // producer thread will wait
            wait();
        }
        queue.add(message);
        // notify that new message is ready to be processed in the queue
        notify();
        System.out.println("Produced :" + message);
    }

    // The thread which access this method will acquire a monitor lock as it is a synchronized method
    public synchronized Message consume() throws Exception {
        while (queue.isEmpty()) {
            System.out.println("Queue is empty, Consumer is waiting for Producer");
            // consumer thread will wait
            wait();
        }
        Message message = queue.poll();
        System.out.println("Consumed :" + message);
        // notify that one message has been consumed
        notify();
        return message;
    }
}
