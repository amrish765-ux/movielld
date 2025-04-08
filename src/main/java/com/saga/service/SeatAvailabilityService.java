package com.saga.service;


import com.saga.model.Seat;
import com.saga.model.Show;
import com.saga.providers.SeatLockProvider;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SeatAvailabilityService {
    private final BookingService bookingService;
    private final SeatLockProvider seatLockProvider;;

    public SeatAvailabilityService(@NonNull final BookingService bookingService,
                                   @NonNull final SeatLockProvider seatLockProvider){
        this.bookingService=bookingService;
        this.seatLockProvider=seatLockProvider;
    }

    public List<Seat>getAvailableSeat(@NonNull final Show show){
        final List<Seat>allSeats=show.getScreen().getSeats();
        final List<Seat>unavailableSeats=getUnavailableSeats(show);

        final List<Seat>availableSeat=new ArrayList<>(allSeats);
        availableSeat.removeAll(unavailableSeats);
        return availableSeat;
    }

    private List<Seat> getUnavailableSeats(@NonNull final Show show) {
        final List<Seat> unAvailableSeats=bookingService.getBookedSeats(show);
        unAvailableSeats.addAll(seatLockProvider.getLockedSeats(show));
        return unAvailableSeats;
    }
}