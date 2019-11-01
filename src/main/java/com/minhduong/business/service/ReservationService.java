package com.minhduong.business.service;

import com.fasterxml.jackson.databind.BeanProperty;
import com.minhduong.business.domain.RoomReservation;
import com.minhduong.data.entity.Guest;
import com.minhduong.data.entity.Reservation;
import com.minhduong.data.entity.Room;
import com.minhduong.data.repository.GuestRepository;
import com.minhduong.data.repository.ReservationRepository;
import com.minhduong.data.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationService {

    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(GuestRepository guestRepository, RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<RoomReservation> getRoomReservationsForDate (Date date) {
        Iterable<Room> rooms = roomRepository.findAll();
        Map<Long, RoomReservation> roomReservationMap = new HashMap<>();

        rooms.forEach(room -> {
            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setRoomId(room.getId());
            roomReservation.setRoomName(room.getName());
            roomReservation.setRoomNumber(room.getNumber());
            roomReservationMap.put(roomReservation.getRoomId(), roomReservation);
        });

        // pull from the reservation table to find all the reservation and update the map with the guest's info
        Iterable<Reservation> reservations = reservationRepository.findByDate(new java.sql.Date(date.getTime()));
        if (reservations != null) {
            reservations.forEach(reservation -> {
                Optional<Guest> guestResponse = guestRepository.findById(reservation.getGuestId());
                if (guestResponse.isPresent()) {
                    Guest guest = guestResponse.get();
                    RoomReservation roomReservation = roomReservationMap.get(reservation.getId());
                    roomReservation.setDate(date);
                    roomReservation.setFirstName(guest.getFirstName());
                    roomReservation.setLastName(guest.getLastName());
                    roomReservation.setGuestId(guest.getId());
                }
            });
        }

        // return all the reservation
        List<RoomReservation> roomReservation = new ArrayList<>();
        for (Long roomId : roomReservationMap.keySet()) {
            roomReservation.add(roomReservationMap.get(roomId));
        }
        return roomReservation;
    }
}
