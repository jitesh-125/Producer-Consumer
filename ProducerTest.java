import com.shubham.messageQueue.Message;
import com.shubham.messageQueue.MessageQueue;
import com.shubham.producer.Producer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProducerTest {

    @Test
    void testProducer() throws Exception {
        MessageQueue messageQueue = new MessageQueue(3);
        List<Message> messages = List.of(new Message("200"), new Message("201"), new Message("202"));

        Producer producer = new Producer(messageQueue, messages);
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producerThread.join();

        assertEquals("200", messageQueue.consume().getStatusCode());
        assertEquals("201", messageQueue.consume().getStatusCode());
        assertEquals("202", messageQueue.consume().getStatusCode());
    }
}
