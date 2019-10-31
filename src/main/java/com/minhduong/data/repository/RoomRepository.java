package com.minhduong.data.repository;

import com.minhduong.data.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    // auto-created query
    Room findByNumber(String number);
}
