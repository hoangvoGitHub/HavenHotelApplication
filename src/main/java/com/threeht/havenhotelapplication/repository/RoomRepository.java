package com.threeht.havenhotelapplication.repository;

import com.threeht.havenhotelapplication.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();
}
