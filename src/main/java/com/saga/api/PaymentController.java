package com.saga.api;

import com.saga.service.BookingService;
import com.saga.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final BookingService bookingService;

    public void paymentFailed(@NonNull final String bookingId,
                              @NonNull String user){
        paymentService.processPayemntFailed(bookingService.getBooking(bookingId),user);

    }
    public void paymentSuccess(@NonNull final String bookingId,
                               @NonNull final String user){
        bookingService.confirmBooking(bookingService.getBooking(bookingId),user);
    }
}
