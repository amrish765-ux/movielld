package com.saga.model;

import com.saga.exception.InvalidStateException;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class Booking {
    private final String id;
    private final Show show;
    private final List<Seat> seatsBooked;
    private final String user;
    private BookingStatus bookingStatus;

    public Booking(@NonNull final String id,@NonNull final Show show,
                   @NonNull final List<Seat> seats,
                   @NonNull final String user) {
        this.id = id;
        this.show = show;
        this.seatsBooked = seats;
        this.user = user;
        this.bookingStatus=BookingStatus.Created;
    }



    public boolean isConfirmed(){return this.bookingStatus==BookingStatus.Confirmed;}

    public void confirmBooking(){
        if(this.bookingStatus!=BookingStatus.Created){
            throw new InvalidStateException();
        }
        this.bookingStatus=BookingStatus.Confirmed;
    }
    public void expireBooking(){
        if (this.bookingStatus!=BookingStatus.Created){
            throw new InvalidStateException();
        }
        this.bookingStatus=BookingStatus.Expired;
    }
}
