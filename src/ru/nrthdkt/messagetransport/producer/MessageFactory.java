package ru.nrthdkt.messagetransport.producer;

import ru.nrthdkt.messagetransport.model.Message;

import static java.lang.String.format;

public final class MessageFactory{
    private static final int INITIAL_NEXT_MESSAGE_INDEX = 1;
    private static final String TEMPLATE_CREATED_MESSAGE_DATA = "Message#%d";
    private int nextMessageIndex;

    public MessageFactory(){
        this.nextMessageIndex = INITIAL_NEXT_MESSAGE_INDEX;
    }
    public Message createMessage() {
        return new Message(format(TEMPLATE_CREATED_MESSAGE_DATA, findAndIncNextMessInd()));
    }
    private synchronized int findAndIncNextMessInd(){
        return this.nextMessageIndex++;
    }
}