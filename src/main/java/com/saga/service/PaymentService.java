package com.saga.service;

import com.saga.exception.BadRequestException;
import com.saga.model.Booking;
import com.saga.providers.SeatLockProvider;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class PaymentService {
    Map<Booking,Integer>bookingFailures;
    private final Integer allowedRetries;
    private final SeatLockProvider seatLockProvider;


    public PaymentService(Integer allowedRetries, SeatLockProvider seatLockProvider) {
        this.allowedRetries = allowedRetries;
        this.seatLockProvider = seatLockProvider;
        bookingFailures=new HashMap<>();
    }

    public void processPayemntFailed(@NonNull final Booking booking,@NonNull final String user){
        if(!booking.getUser().equals(user)){
            throw new BadRequestException();
        }
        if(!bookingFailures.containsKey(booking)){
            bookingFailures.put(booking,0);
        }
        final Integer currFailureCount=bookingFailures.get(booking);
        final Integer newFailureCount=currFailureCount+1;
        bookingFailures.put(booking,newFailureCount);
        if(newFailureCount>allowedRetries){
            seatLockProvider.unlockSeats(booking.getShow(),booking.getSeatsBooked(),booking.getUser());
        }
    }
}
