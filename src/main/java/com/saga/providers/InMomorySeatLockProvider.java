package com.saga.providers;

import com.saga.exception.SeatTemporaryUnavailableException;
import com.saga.model.Seat;
import com.saga.model.SeatLock;
import com.saga.model.Show;
import lombok.NonNull;
import com.google.common.collect.ImmutableList;
import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class InMomorySeatLockProvider implements SeatLockProvider{
    private final Integer lockTimeOut;
    private final Map<Show,Map<Seat, SeatLock>>locks;


    public InMomorySeatLockProvider(@NonNull final Integer lockTimeOut) {
        this.lockTimeOut = lockTimeOut;
        locks=new HashMap<>();
    }


    @Override
    synchronized public void lockSeats(@NonNull final Show show,
                                       @NonNull final List<Seat> seats,
                                       @NonNull final String user) {

        for(Seat seat:seats){
            if(isSeatLocked(show,seat)){
                throw new SeatTemporaryUnavailableException();
            }
        }
        for(Seat seat:seats){
            lockSeat(show,seat,user,lockTimeOut);
        }
    }

    private boolean isSeatLocked(final Show show,final Seat seat) {
        return locks.containsKey(show)&&locks.get(show).containsKey(seat)&&locks.get(show).get(seat).isLockExpired();
    }

    private void lockSeat(Show show,
                          Seat seat,
                          String user,
                          Integer lockTimeOut) {
        if (!locks.containsKey(show)){
            locks.put(show,new HashMap<>());
        }
        final SeatLock lock=new SeatLock(seat,show,lockTimeOut,new Date(),user);
        locks.get(show).put(seat,lock);
    }

    @Override
    public void unlockSeats(@NonNull final Show show,
                            @NonNull final List<Seat> seats,
                            @NonNull final String user) {
        for(Seat seat:seats){
            if(validateLock(show,seat,user)){
                unlockSeat(show,seat);
            }
        }
    }

    private void unlockSeat(final Show show,final Seat seat) {
        if (!locks.containsKey(show)){
            return;
        }
        locks.get(show).remove(seat);
    }

    @Override
    public boolean validateLock(Show show, Seat seat, String user) {
        return isSeatLocked(show,seat)&&locks.get(show).get(seat).getLockedBy().equals(user);
    }

    @Override
    public List<Seat> getLockedSeats(Show show) {
        if (!locks.containsKey(show)){
            return ImmutableList.of();
        }
        final List<Seat>lockedSeats=new ArrayList<>();
        for(Seat seat:locks.get(show).keySet()){
            if(isSeatLocked(show,seat)){
                lockedSeats.add(seat);
            }
        }
        return lockedSeats;
    }
}
