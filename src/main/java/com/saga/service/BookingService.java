package com.saga.service;

import com.saga.exception.BadRequestException;
import com.saga.exception.NotFoundException;
import com.saga.exception.SeatPermanentlyUnavailableException;
import com.saga.model.Booking;
import com.saga.model.Seat;
import com.saga.model.Show;
import com.saga.providers.SeatLockProvider;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class BookingService {
    private final Map<String, Booking>showBookings;
    private final SeatLockProvider seatLockProvider;

    public BookingService(SeatLockProvider seatLockProvider) {
        this.seatLockProvider = seatLockProvider;
        showBookings=new HashMap<>();
    }

    public Booking getBooking(@NonNull final String bookingId){
        if(!showBookings.containsKey(bookingId)){
            throw new NotFoundException();
        }
        return showBookings.get(bookingId);
    }

    public List<Booking> getAllBooking(@NonNull final Show show){
        List<Booking>response=new ArrayList<>();
        for(Booking booking:showBookings.values()){
            if(booking.getShow().equals(show)){
                response.add(booking);
            }
        }
        return response;
    }
    public Booking createBooking(@NonNull final String userId, @NonNull final Show show,
                                 @NonNull final List<Seat>seats){
        if(isAnySeatAlreadyBooked(show,seats)){
            throw new SeatPermanentlyUnavailableException("seat already booked by someone");
        }
        seatLockProvider.lockSeats(show,seats,userId);
        final String bookingId=UUID.randomUUID().toString();
        final Booking newBooking=new Booking(bookingId,show,seats,userId);
        showBookings.put(bookingId,newBooking);
        return newBooking;

    }
    public List<Seat>getBookedSeats(@NonNull final Show show){
        return getAllBooking(show)
                .stream()
                .filter(Booking::isConfirmed)
                .map(Booking::getSeatsBooked)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public void confirmBooking(@NonNull final Booking booking,@NonNull final String user){
        if(!booking.getUser().equals(user)){
            throw new BadRequestException();
        }

        for(Seat seat:booking.getSeatsBooked()){
            if(!seatLockProvider.validateLock(booking.getShow(),seat,user)){
                throw new BadRequestException();
            }
        }
        booking.confirmBooking();
    }



    private boolean isAnySeatAlreadyBooked(Show show, List<Seat> seats) {
        final List<Seat>bookedSeats=getBookedSeats(show);
        for (Seat seat : seats) {
            if (bookedSeats.contains(seat)) {
                return true;
            }
        }
        return false;
    }
}
