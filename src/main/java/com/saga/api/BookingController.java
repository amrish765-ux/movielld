package com.saga.api;

import com.saga.model.Booking;
import com.saga.model.Seat;
import com.saga.model.Show;
import com.saga.service.BookingService;
import com.saga.service.ShowService;
import com.saga.service.TheatreService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BookingController {
    private final ShowService showService;
    private final TheatreService theatreService;
    private final BookingService bookingService;

    public String createBooking(@NonNull final String userId,
                                @NonNull final String showId,
                                @NonNull final List<String>seatIds){
        final Show show=showService.getShow(showId);
        final List<Seat>seats=seatIds.stream().map(theatreService::getSeat).collect(Collectors.toList());
        return bookingService.createBooking(userId,show,seats).getId();
    }
}
