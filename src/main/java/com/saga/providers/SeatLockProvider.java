package com.saga.providers;

import com.saga.model.Seat;
import com.saga.model.Show;

import java.util.List;

public interface SeatLockProvider {
    void lockSeats(Show show, List<Seat>seats,String user);
    void unlockSeats(Show show,List<Seat>seats,String user);
//    boolean validateSeat(Show show,Seat seat,String user);

    boolean validateLock(Show show, Seat seat, String user);

    List<Seat>getLockedSeats(Show show);
}
