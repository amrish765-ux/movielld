package com.saga.api;

import com.saga.model.Movie;
import com.saga.model.Screen;
import com.saga.model.Seat;
import com.saga.model.Show;
import com.saga.service.MovieService;
import com.saga.service.SeatAvailabilityService;
import com.saga.service.ShowService;
import com.saga.service.TheatreService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
public class ShowController {
    private final SeatAvailabilityService seatAvailabilityService;
    private final ShowService showService;
    private final TheatreService theatreService;
    private final MovieService movieService;


    public String createShow(@NonNull final String movieId,
                             @NonNull final String screenId,
                             @NonNull final Date startTime,
                             @NonNull final Integer durationInSeconds){
        final Screen screen=theatreService.getScreen(screenId);
        final Movie movie=movieService.getMovie(movieId);
        return showService.createShow(movie,screen,startTime,durationInSeconds).getId();
    }

    public List<String>getAvailableSeats(@NonNull final String showId){
        final Show show=showService.getShow(showId);
        return seatAvailabilityService
                .getAvailableSeat(show)
                .stream()
                .map(Seat::getId)
                .collect(Collectors.toList());
    }

}
