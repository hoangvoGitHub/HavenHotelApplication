package com.threeht.havenhotelapplication.repository;

import com.threeht.havenhotelapplication.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookedRoomRepository extends JpaRepository<BookedRoom, Long> {
}
