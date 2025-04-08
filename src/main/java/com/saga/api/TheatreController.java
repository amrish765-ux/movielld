package com.saga.api;

import com.saga.model.Screen;
import com.saga.model.Seat;
import com.saga.model.Theatre;
import com.saga.service.TheatreService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class TheatreController {
    private final TheatreService theatreService;

    public String createThreatre(@NonNull final String theatreName){
        return theatreService.createTheatre(theatreName).getId();
    }
    public String createScreenInThreatre(@NonNull final String screenName,
                                         @NonNull final String theatreId){
        final Theatre theatre=theatreService.getTheatre(theatreId);
        return theatreService.createScreenInTheatre(screenName,theatre).getId();
    }
    public String createSeatInScreen(@NonNull final Integer rowNo, @NonNull final Integer colNo, @NonNull final String screenId){
        final Screen screen=theatreService.getScreen(screenId);
//        System.out.println(screen.getName());
        return theatreService.createSeatInScreen(rowNo,colNo,screen).getId();
    }
}
