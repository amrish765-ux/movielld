package com.saga.model;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class Screen {
    private final String id;
    private final String name;
    private final Theatre theatre;
    private final List<Seat>seats;

    public Screen(@NonNull String id,@NonNull String name,@NonNull Theatre theatre) {
        this.id = id;
        this.name = name;
        this.theatre = theatre;
        seats=new ArrayList<>();
    }
    public void addSeat(@NonNull final Seat seat){
        this.seats.add(seat);
    }
}
