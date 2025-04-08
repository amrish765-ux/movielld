package com.saga.service;

import com.saga.exception.NotFoundException;
import com.saga.model.Screen;
import com.saga.model.Seat;
import com.saga.model.SeatLock;
import com.saga.model.Theatre;
import lombok.NonNull;

import javax.lang.model.element.NestingKind;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TheatreService {
    private final Map<String, Theatre>theatreMap;
    private final Map<String, Screen>screenMap;
    private final Map<String, Seat>seatMap;

    public TheatreService(){
        theatreMap=new HashMap<>();
        screenMap=new HashMap<>();
        seatMap=new HashMap<>();
    }

    public Seat getSeat(@NonNull final String seatId){
        if (!seatMap.containsKey(seatId)){
            throw new NotFoundException();
        }
        return seatMap.get(seatId);
    }

    public Theatre getTheatre(@NonNull final String theatreId){
        if (!theatreMap.containsKey(theatreId)){
            throw new NotFoundException();
        }
        return theatreMap.get(theatreId);
    }

    public Screen getScreen(@NonNull final String screenId){
        if (!screenMap.containsKey(screenId)){
            throw new NotFoundException();
        }
        return screenMap.get(screenId);
    }

    public Theatre createTheatre(@NonNull final String theatreName){
        String theatreId= UUID.randomUUID().toString();
        Theatre theatre=new Theatre(theatreId,theatreName);
        theatreMap.put(theatreId,theatre);
        return theatre;
    }

    public Screen createScreenInTheatre(@NonNull final String screenName,@NonNull final Theatre theatre){
        Screen screen=createScreen(screenName,theatre);
        theatre.addScreen(screen);
        screenMap.put(screen.getId(),screen);
        return screen;
    }

    public Seat createSeatInScreen(@NonNull final Integer rowNo,
                                   @NonNull final Integer seatNo,
                                   @NonNull final Screen screen){
        String seatId=UUID.randomUUID().toString();
        Seat seat=new Seat(seatId,rowNo,seatNo);
        seatMap.put(seatId,seat);
        screen.addSeat(seat);
        return seat;
    }



    private Screen createScreen(String screenName, Theatre theatre) {
        String screenId=UUID.randomUUID().toString();
        return new Screen(screenId,screenName,theatre);
    }

}
