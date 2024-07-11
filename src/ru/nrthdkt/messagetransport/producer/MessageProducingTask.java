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

    public MessageProducingTask(final MessageBroker messageBroker){
        this.messageBroker = messageBroker;
        this.messageFactory = new MessageFactory();
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

    private static final class MessageFactory{
        private static final int INITIAL_NEXT_MESSAGE_INDEX = 1;
        private static final String TEMPLATE_CREATED_MESSAGE_DATA = "Message#%d";
        private int nextMessageIndex;

        public MessageFactory(){
            this.nextMessageIndex = INITIAL_NEXT_MESSAGE_INDEX;
        }
        public Message createMessage(){
            return new Message(format(TEMPLATE_CREATED_MESSAGE_DATA,this.nextMessageIndex++));
        }
    }
}
