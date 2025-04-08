package com.saga.service;

import com.saga.exception.NotFoundException;
import com.saga.model.Movie;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MovieService {
    private final Map<String, Movie>movieMap;

    public MovieService() {
        this.movieMap = new HashMap<>();
    }

    public Movie getMovie(@NonNull final String movieId){
        if(!movieMap.containsKey(movieId)){
            throw new NotFoundException();
        }
        return movieMap.get(movieId);
    }

    public Movie createMovie(@NonNull final String movieName){
        String movieId= UUID.randomUUID().toString();
        final Movie movie=new Movie(movieId,movieName);
        movieMap.put(movieId,movie);
        return movie;
    }
}
