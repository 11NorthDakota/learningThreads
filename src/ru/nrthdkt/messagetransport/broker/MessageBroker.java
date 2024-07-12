package ru.nrthdkt.messagetransport.broker;

import ru.nrthdkt.messagetransport.consumer.MessageConsumingTask;
import ru.nrthdkt.messagetransport.model.Message;
import ru.nrthdkt.messagetransport.producer.MessageProducingTask;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

import static java.util.Optional.*;

public class MessageBroker {
    private static final String TEMPLATE_MESSAGE_MESPROD = "Message '%s' is produced by producer '%s'. " +
            "Amount of messages before producing: %d.\n";
    private static final String TEMPLATE_MESSAGE_MESCONS = "Message '%s' is consuming by '%s'." +
            "Amount of messages before consuming %d.\n";
    private final Queue<Message> messagesToBeConsumed;
    private final int maxStoredMessages;

    public MessageBroker(final int maxStoredMessages) {
        this.maxStoredMessages = maxStoredMessages;
        this.messagesToBeConsumed = new ArrayDeque<>(maxStoredMessages);
    }

    public synchronized void produce(final Message message,final MessageProducingTask producingTask) {
        try {
            while (!this.isShouldProduce(producingTask)){
                super.wait();
            }
            this.messagesToBeConsumed.add(message);
            System.out.printf(TEMPLATE_MESSAGE_MESPROD,message,producingTask.getName(),this.messagesToBeConsumed.size()-1);
            super.notify();
        }
        catch (final InterruptedException exception){
            Thread.currentThread().interrupt();
        }
    }

    public synchronized Optional<Message> consume(final MessageConsumingTask consumingTask) {
        try{
            while(this.isShouldConsume(consumingTask)){
                super.wait();
            }
            final Message consumedMessage = messagesToBeConsumed.poll();
            System.out.printf(TEMPLATE_MESSAGE_MESCONS,consumedMessage,consumingTask.getName(),this.messagesToBeConsumed.size()+1);
            super.notify();
            return ofNullable(consumedMessage);
        }
        catch(final InterruptedException exception){
            Thread.currentThread().interrupt();
            return empty();
        }
    }

    private boolean isShouldProduce(final MessageProducingTask producingTask){
        return this.messagesToBeConsumed.size() < this.maxStoredMessages &&
                this.messagesToBeConsumed.size() <= producingTask.getMaximalMessageAmountToProduce();
    }

    private boolean isShouldConsume(final MessageConsumingTask consumingTask){
        return !this.messagesToBeConsumed.isEmpty() &&
                this.messagesToBeConsumed.size()>=consumingTask.getMinMessageAmountToConsume();
    }
}
