package com.threeht.havenhotelapplication.service;

import com.threeht.havenhotelapplication.exception.InvalidBookingRequestException;
import com.threeht.havenhotelapplication.exception.ResourceNotFoundException;
import com.threeht.havenhotelapplication.model.BookedRoom;
import com.threeht.havenhotelapplication.model.Room;
import com.threeht.havenhotelapplication.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingSerivce implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public BookedRoom findBookingByConfirmationCode(String confirmationCode) {
        return bookingRepository.findBookingByConfirmationCode(confirmationCode)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No booking found with booking code :" + confirmationCode));
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
        if (roomIsAvailable) {
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        } else {
            throw new InvalidBookingRequestException("Sorry, This room is not available for the selected dates;");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }
    
    @Override
    public void cancleBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(
                    existingBooking -> bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                            // existCheckInDate <-------- requestCheckInDate ----> existCheckOutDate
                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                            // requestCheckInDate ---> existCheckInDate && requestCheckOutDate === existCheckOutDate
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                            // requestCheckInDate ---> existCheckInDate &&  existCheckOutDate <--- requestCheckOutDate
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))
                            // requestCheckInDate === existCheckInDate &&  existCheckOutDate === requestCheckOutDate
                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
                            // requestCheckInDate === existCheckInDate && requestCheckInDate === requestCheckInDate
                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate())));
    }

}
