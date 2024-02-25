package com.threeht.havenhotelapplication.repository;

import com.threeht.havenhotelapplication.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
    List<BookedRoom> findByRoomId(Long roomId);

    Optional<BookedRoom> findBookingByConfirmationCode(String confirmationCode);

    List<BookedRoom> findByGuestEmail(String email);

}