package ru.nrthdkt.messagetransport.consumer;

import ru.nrthdkt.messagetransport.broker.MessageBroker;
import ru.nrthdkt.messagetransport.model.Message;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MessageConsumingTask implements Runnable{
    private final MessageBroker messageBroker;
    private static final int messageDelay = 5;
    private static final String TEMPLATE_MESSAGE_MESCONS = "Message '%s' is consuming\n";
    public MessageConsumingTask(MessageBroker messageBroker){
        this.messageBroker = messageBroker;
    }

    @Override
    public void run() {
        try{
            while(!Thread.currentThread().isInterrupted()){
                TimeUnit.SECONDS.sleep(messageDelay);
                final Optional<Message> optionalConsumedMessage = this.messageBroker.consume();
                final Message consumedMessage = optionalConsumedMessage.orElseThrow(
                        MessageConsumingException::new);
                System.out.printf(TEMPLATE_MESSAGE_MESCONS,consumedMessage);
            }
        }catch(InterruptedException exception){
            System.out.println(exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
