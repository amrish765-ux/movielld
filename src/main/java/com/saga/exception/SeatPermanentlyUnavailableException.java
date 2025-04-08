package com.saga.exception;

public class SeatPermanentlyUnavailableException extends RuntimeException{
    public SeatPermanentlyUnavailableException(String msg){
        super(msg);
    }
}
