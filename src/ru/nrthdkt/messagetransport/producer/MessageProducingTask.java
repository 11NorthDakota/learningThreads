package ru.nrthdkt.messagetransport.producer;

import ru.nrthdkt.messagetransport.broker.MessageBroker;
import ru.nrthdkt.messagetransport.model.Message;

import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

public class MessageProducingTask implements Runnable{
    private final MessageBroker messageBroker;
    private final MessageFactory messageFactory;
    private static final String TEMPLATE_MESSAGE_MESPROD = "Message '%s' is produced\n";
    private static final int messageDelay = 1;

    public MessageProducingTask(final MessageBroker messageBroker,final MessageFactory messageFactory){
        this.messageBroker = messageBroker;
        this.messageFactory = messageFactory;
    }
    @Override
    public void run() {
        try{
            while(!Thread.currentThread().isInterrupted()){
                final Message producedMessage = this.messageFactory.createMessage();
                this.messageBroker.produce(producedMessage);
                TimeUnit.SECONDS.sleep(messageDelay);
                System.out.printf(TEMPLATE_MESSAGE_MESPROD,producedMessage);
            }
        }catch(InterruptedException exception){
            System.out.println(exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}
