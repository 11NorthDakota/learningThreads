package ru.nrthdkt.messagetransport.consumer;

import ru.nrthdkt.messagetransport.broker.MessageBroker;
import ru.nrthdkt.messagetransport.model.Message;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MessageConsumingTask implements Runnable {
    private final MessageBroker messageBroker;
    private static final int messageDelay = 1;

    private final int minMessageAmountToConsume;
    private final String name;

    public int getMinMessageAmountToConsume() {
        return minMessageAmountToConsume;
    }

    public MessageConsumingTask(final MessageBroker messageBroker, final int minMessageAmountToConsume, final String name) {
        this.messageBroker = messageBroker;
        this.minMessageAmountToConsume = minMessageAmountToConsume;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                TimeUnit.SECONDS.sleep(messageDelay);
                final Optional<Message> optionalConsumedMessage = this.messageBroker.consume(this);
                optionalConsumedMessage.orElseThrow(MessageConsumingException::new);
            }
        } catch (InterruptedException exception) {
            System.out.println(exception.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
