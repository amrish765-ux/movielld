package com.saga;

import com.saga.api.*;
import com.saga.exception.SeatPermanentlyUnavailableException;
import com.saga.providers.InMomorySeatLockProvider;
import com.saga.providers.SeatLockProvider;
import com.saga.service.*;


import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class MovieTicketBook
{
    public static void main( String[] args )
    {
        MovieService movieService=new MovieService();
        TheatreService theatreService=new TheatreService();
        ShowService showService=new ShowService();

        SeatLockProvider seatLockProvider=new InMomorySeatLockProvider(600);

        BookingService bookingService=new BookingService(seatLockProvider);

        SeatAvailabilityService seatAvailabilityService=new SeatAvailabilityService(bookingService,seatLockProvider);

        PaymentService paymentService=new PaymentService(3,seatLockProvider);

        MovieController movieController=new MovieController(movieService);
        TheatreController theatreController=new TheatreController(theatreService);
        ShowController showController=new ShowController(seatAvailabilityService,showService,theatreService,movieService);
        BookingController bookingController=new BookingController(showService,theatreService,bookingService);
        PaymentController paymentController=new PaymentController(paymentService,bookingService);


        try{
//           1. create movie
            String movieId=movieController.createMovie("hey");
            System.out.println("created movie with id "+movieId);



//            2. create theatre
            String theatreId=theatreController.createThreatre("PVR");
            System.out.println("theatre created with id "+theatreId);


//            3.create screen in theatre
            String screenId=theatreController.createScreenInThreatre("screen-1",theatreId);
            System.out.println("screen created with id "+screenId);

//            Scanner sc=new Scanner(System.in);

            for(int row=0;row<=5;row++){
                for(int seatno=0;seatno<=10;seatno++){
                    String seatid=theatreController.createSeatInScreen(row,seatno,screenId);
                    System.out.println("seat is created with id "+seatid+" at rowno "+row+" and seatno "+seatno);
                }
            }

//            5.create show
            Date showStartTime=new Date();
            int durationInMinutes=180;

            String showId=showController.createShow(movieId,screenId,showStartTime,durationInMinutes);
            System.out.println("created show id "+showId+" starting at "+durationInMinutes);

//            6.get available seat for the show
            List<String>availableseats=showController.getAvailableSeats(showId);
            System.out.println("availableseats for the show "+availableseats.size());

//            7.book selected seats

            List<String>seatstobook=availableseats.subList(0,2);

            String bookingid=bookingController.createBooking("amrish",showId,seatstobook);

            System.out.println("create booking with id "+bookingid+" booked seats: "+seatstobook);
//            8. payment
            System.out.println("processing payment....");
            paymentController.paymentSuccess(bookingid,"amrish");
            System.out.println("payment successful!!");

//            9.  check the availableseats again;

            List<String>reminaingseates=showController.getAvailableSeats(showId);

            System.out.println("remaining available seats "+reminaingseates.size());

            System.out.println("number of seats booked "+(availableseats.size()-reminaingseates.size()));
//            10. demonstrate the failed payment

            List<String>moreseattobook=availableseats.subList(3,8);
            String failedBookingId=bookingController.createBooking("amrish",showId,moreseattobook);
            System.out.println("create another booking with id "+failedBookingId+" for seats "+moreseattobook);

            paymentController.paymentFailed(bookingid,"amrish");
            System.out.println("payment failed for booking id "+bookingid);

//            wait for a while to see if we get the seat unlock after multiple failure
            System.out.println("simulating multiple payment failure...");
            for(int i=0;i<3;i++){
                paymentController.paymentFailed(bookingid,"amrish");
                System.out.println("payment attempt "+(i+2));
            }


//            check if seats were unlocked
            List<String>seatsAfterFailure=showController.getAvailableSeats(showId);
            System.out.println("available seats after payment failure "+seatsAfterFailure.size());


        } catch (SeatPermanentlyUnavailableException e1){
            System.out.println("Error: " + e1.getMessage());
        }
        catch (Exception e) {
//            System.out.println("error message"+e.printStackTrace());
            e.printStackTrace();
        }

    }
}
