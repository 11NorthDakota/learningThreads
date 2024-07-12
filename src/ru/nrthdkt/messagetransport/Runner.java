package ru.nrthdkt.messagetransport;

import ru.nrthdkt.messagetransport.broker.MessageBroker;
import ru.nrthdkt.messagetransport.consumer.MessageConsumingTask;
import ru.nrthdkt.messagetransport.producer.MessageFactory;
import ru.nrthdkt.messagetransport.producer.MessageProducingTask;

import static java.util.Arrays.stream;

public class Runner {
    public static void main(String[] args){
        final int brokerMaxStoredMessage = 15;
        final MessageBroker messageBroker = new MessageBroker(brokerMaxStoredMessage);

        final MessageFactory messageFactory = new MessageFactory();

        final Thread firstProducingThread = new Thread(new MessageProducingTask(messageBroker,messageFactory,
                brokerMaxStoredMessage,"First Producer"));
        final Thread secondProducingThread = new Thread(new MessageProducingTask(messageBroker,messageFactory,10,"Second Producer"));
        final Thread thirdProducingThread = new Thread(new MessageProducingTask(messageBroker,messageFactory,5,"Third Producer"));

        final Thread firstConsumerThread = new Thread(new MessageConsumingTask(messageBroker,0,"First Consumer"));
        final Thread secondConsumerThread = new Thread(new MessageConsumingTask(messageBroker,6,"Second Consumer"));
        final Thread thirdConsumerThread = new Thread(new MessageConsumingTask(messageBroker,11,"Third Consumer"));

       startThreads(firstProducingThread,secondProducingThread,thirdProducingThread,
               firstConsumerThread,secondConsumerThread,thirdConsumerThread);
    }
    private static void startThreads(final Thread... threads){
        stream(threads).forEach(Thread::start);
    }
}
