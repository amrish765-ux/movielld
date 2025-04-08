package com.saga.service;

import com.saga.exception.NotFoundException;
import com.saga.exception.ScreenAlreadyOccupiedException;
import com.saga.model.Movie;
import com.saga.model.Screen;
import com.saga.model.Show;
import lombok.NonNull;

import java.util.*;

public class ShowService {

    private final Map<String , Show>showMap;
    public ShowService(){
        showMap=new HashMap<>();
    }

    public Show getShow(@NonNull final String showId){
        if(!showMap.containsKey(showId)){
            throw new NotFoundException();
        }
        return showMap.get(showId);
    }

    public Show createShow(@NonNull final Movie movie, @NonNull final Screen screen,@NonNull final Date startTime,
                           @NonNull final Integer durationInSeconds){
        if(!checkIfShowCreationAllowed(screen,startTime,durationInSeconds)){
            throw new ScreenAlreadyOccupiedException();
        }
        String showId= UUID.randomUUID().toString();
        final Show show=new Show(showId,movie,screen,startTime,durationInSeconds);
        this.showMap.put(showId,show);
        return show;
    }

    private boolean checkIfShowCreationAllowed(Screen screen, Date startTime, Integer durationInSeconds) {
        return true;
    }

    private List<Show>getShowForScreen(final Screen screen){
        final List<Show>shows=new ArrayList<>();
        for(Show show:showMap.values()){
            if (show.getScreen().equals(screen)){
                shows.add(show);
            }
        }
        return shows;
    }




}
