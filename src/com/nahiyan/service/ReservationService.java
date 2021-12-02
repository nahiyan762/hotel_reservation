package com.nahiyan.service;

import com.nahiyan.model.Customer;
import com.nahiyan.model.IRoom;
import com.nahiyan.model.Reservation;

import java.util.*;

public final class ReservationService {
    private static ReservationService instance;

    private ReservationService() {
    }

    private final Map<String, IRoom> roomMap = new HashMap<>();
    private final Map<String, Collection<Reservation>> reservationMap = new HashMap<>();

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public Map<String, IRoom> getRoomMap() {
        return roomMap;
    }

    public void addRoom(IRoom room) {
        roomMap.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomNumber) {
        return roomMap.get(roomNumber);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        Collection<Reservation> customerReservations = reservationMap.get(customer.getEmail());
        if (customerReservations == null) {
            customerReservations = new ArrayList<>();
        }
        customerReservations.add(reservation);
        reservationMap.put(customer.getEmail(), customerReservations);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate){

        if (reservationMap.size() > 0){
            Collection<IRoom> reservedRooms = new ArrayList<>();
            Collection<IRoom> availableRooms = new ArrayList<>();
            for (Collection<Reservation> reservations: reservationMap.values()) {
                for (Reservation reservation : reservations) {
                    if (reservation.isRoomReserved(checkInDate, checkOutDate)) {
                        reservedRooms.add(reservation.getRoom());
                    }
                }
            }

            for (IRoom iRoom : roomMap.values()){
                boolean isRoomAvailable = true;
                for (IRoom iRoom1 : reservedRooms) {
                    if (iRoom.getRoomNumber().equals(iRoom1.getRoomNumber())) {
                        isRoomAvailable = false;
                    }
                }
                if (isRoomAvailable){
                    availableRooms.add(iRoom);
                }
            }

            return availableRooms;
        } else {
            return roomMap.values();
        }

    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        return reservationMap.get(customer.getEmail());
    }

    public void printAllReservation() {
        for (Collection<Reservation> reservations : reservationMap.values()) {
            for (Reservation reservation : reservations) {
                System.out.println(reservation.toString());
            }
        }
    }
}
