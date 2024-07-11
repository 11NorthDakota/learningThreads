package ru.nrthdkt.messagetransport.consumer;

public class MessageConsumingException extends RuntimeException{
    public MessageConsumingException(){

    }
    public MessageConsumingException(final String desciption){
        super(desciption);
    }
    public MessageConsumingException(final Exception cause){
        super(cause);
    }
    public MessageConsumingException(final String desciption,final Exception cause){
        super(desciption,cause);
    }
}
