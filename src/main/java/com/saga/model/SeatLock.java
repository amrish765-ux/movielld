package com.saga.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@Getter
public class SeatLock {
    private Seat seat;
    private Show show;
    private Integer timeOutInSeconds;
    private Date lockTime;
    private String lockedBy;

    public boolean isLockExpired(){
        final Instant lockInstant=lockTime.toInstant().plusSeconds(timeOutInSeconds);
        final Instant currInstant=new Date().toInstant();
        return currInstant.isBefore(lockInstant);
    }

}
