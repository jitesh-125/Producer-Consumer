import com.shubham.consumer.Consumer;
import com.shubham.messageQueue.Message;
import com.shubham.messageQueue.MessageQueue;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsumerTest {

    @Test
    void testConsumerSuccess() throws Exception {
        MessageQueue messageQueue = new MessageQueue(5);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();

        messageQueue.produce(new Message("200"));
        messageQueue.produce(new Message("201"));
        messageQueue.produce(new Message("202"));
        messageQueue.produce(new Message("TERMINATE"));

        Consumer consumer = new Consumer(messageQueue, successCount, errorCount);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumerThread.join(500);

        assertEquals(3, successCount.get());
        assertEquals(0, errorCount.get());
    }

    @Test
    void testConsumerFailure() throws Exception {
        MessageQueue messageQueue = new MessageQueue(5);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();

        messageQueue.produce(new Message("200"));
        messageQueue.produce(new Message("404"));
        messageQueue.produce(new Message("500"));
        messageQueue.produce(new Message("TERMINATE"));

        Consumer consumer = new Consumer(messageQueue, successCount, errorCount);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumerThread.join(500);

        assertEquals(1, successCount.get());
        assertEquals(2, errorCount.get());
    }

    @Test
    void testConsumerTermination() throws Exception {
        MessageQueue messageQueue = new MessageQueue(3);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();

        messageQueue.produce(new Message("TERMINATE"));

        Consumer consumer = new Consumer(messageQueue, successCount, errorCount);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumerThread.join(500);

        assertEquals(0, successCount.get());
        assertEquals(0, errorCount.get());
    }

    @Test
    void testMultipleConsumers() throws Exception {
        MessageQueue messageQueue = new MessageQueue(6);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger errorCount = new AtomicInteger();

        messageQueue.produce(new Message("200"));
        messageQueue.produce(new Message("404"));
        messageQueue.produce(new Message("500"));
        messageQueue.produce(new Message("TERMINATE"));
        messageQueue.produce(new Message("TERMINATE"));

        Consumer consumer = new Consumer(messageQueue, successCount, errorCount);
        Thread consumerThread1 = new Thread(consumer);
        Thread consumerThread2 = new Thread(consumer);
        consumerThread1.start();
        consumerThread2.start();
        consumerThread1.join(500);
        consumerThread2.join(500);

        assertEquals(1, successCount.get());
        assertEquals(2, errorCount.get());
    }
}
