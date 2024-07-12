package ru.nrthdkt.messagetransport.producer;

import ru.nrthdkt.messagetransport.broker.MessageBroker;
import ru.nrthdkt.messagetransport.model.Message;

import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

public class MessageProducingTask implements Runnable {
    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;
    private static final int messageDelay = 1;
    private final int maximalMessageAmountToProduce;
    private final String name;

    public MessageProducingTask(final MessageBroker messageBroker, final MessageFactory messageFactory,
                                final int maximalMessageAmountToProduce,final String name) {
        this.messageBroker = messageBroker;
        this.messageFactory = messageFactory;
        this.maximalMessageAmountToProduce = maximalMessageAmountToProduce;
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public int getMaximalMessageAmountToProduce(){
        return this.maximalMessageAmountToProduce;
    }
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(messageDelay);
                final Message producedMessage = this.messageFactory.createMessage();
                this.messageBroker.produce(producedMessage,this);
            }
        } catch (InterruptedException exception) {
            System.out.println(exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}
