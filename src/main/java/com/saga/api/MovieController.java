package com.saga.api;

import com.saga.service.MovieService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class MovieController {
    private final MovieService movieService;

    public String createMovie(@NonNull final String movieName){
       return movieService.createMovie(movieName).getId();
    }
}
