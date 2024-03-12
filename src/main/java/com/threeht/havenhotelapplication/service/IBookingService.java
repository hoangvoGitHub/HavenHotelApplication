package com.threeht.havenhotelapplication.service;

import com.threeht.havenhotelapplication.model.BookedRoom;

import java.util.List;

public interface IBookingService {
    List<BookedRoom> getAllBookings();

    BookedRoom findBookingByConfirmationCode(String confirmationCode);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    List<BookedRoom> getBookingsByUserEmail(String email);

    void cancleBooking(Long bookingId);
}
