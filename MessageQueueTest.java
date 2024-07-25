import com.shubham.messageQueue.Message;
import com.shubham.messageQueue.MessageQueue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageQueueTest {

    @Test
    void testProduceAndConsume() throws Exception {
        MessageQueue messageQueue = new MessageQueue(3);
        Message message = new Message("200");
        messageQueue.produce(message);

        Message consumedMessage = messageQueue.consume();
        assertEquals(message, consumedMessage);
    }

    @Test
    void testQueueIsFull() throws Exception {
        MessageQueue messageQueue = new MessageQueue(2);
        messageQueue.produce(new Message("200"));
        messageQueue.produce(new Message("201"));

        // Test the producer is waiting when the queue is full
        Thread producerThread = new Thread(() -> {
            try {
                messageQueue.produce(new Message("202"));
            } catch (Exception e) {
                fail("Producer should wait instead of throwing an exception");
            }
        });
        producerThread.start();
        Thread.sleep(500); // Ensure the producer thread is waiting
        assertTrue(producerThread.isAlive());

        // Consume a message to free space in the queue
        messageQueue.consume();
        producerThread.join();
    }

    @Test
    void testQueueIsEmpty() throws Exception {
        MessageQueue messageQueue = new MessageQueue(2);

        // Test the consumer is waiting when the queue is empty
        Thread consumerThread = new Thread(() -> {
            try {
                messageQueue.consume();
            } catch (Exception e) {
                fail("Consumer should wait instead of throwing an exception");
            }
        });
        consumerThread.start();
        Thread.sleep(500); // Ensure the consumer thread is waiting
        assertTrue(consumerThread.isAlive());

        // Produce a message to wake up the consumer
        messageQueue.produce(new Message("200"));
        consumerThread.join();
    }
}
