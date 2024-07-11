package ru.nrthdkt.messagetransport;

import ru.nrthdkt.messagetransport.broker.MessageBroker;
import ru.nrthdkt.messagetransport.consumer.MessageConsumingTask;
import ru.nrthdkt.messagetransport.producer.MessageProducingTask;

public class Runner {
    public static void main(String[] args){
        final int maxStoredMessage = 5;
        final MessageBroker messageBroker = new MessageBroker(maxStoredMessage);

        final Thread producingThread = new Thread(new MessageProducingTask(messageBroker));
        final Thread consumerThread = new Thread(new MessageConsumingTask(messageBroker));

        producingThread.start();
        consumerThread.start();

    }

}
