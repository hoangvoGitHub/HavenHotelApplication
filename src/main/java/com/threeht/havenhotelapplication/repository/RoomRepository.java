package com.threeht.havenhotelapplication.repository;

import com.threeht.havenhotelapplication.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
