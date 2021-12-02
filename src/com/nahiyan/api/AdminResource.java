package com.nahiyan.api;

import com.nahiyan.model.Customer;
import com.nahiyan.model.IRoom;
import com.nahiyan.model.Room;
import com.nahiyan.model.RoomType;
import com.nahiyan.service.CustomerService;
import com.nahiyan.service.ReservationService;

import java.util.Collection;
import java.util.List;

public final class AdminResource {

    private static AdminResource instance;

    private AdminResource() {
    }

    public static AdminResource getInstance() {
        if (instance == null) {
            instance = new AdminResource();
        }
        return instance;
    }

    public Customer getCustomer(String email) {
        return CustomerService.getInstance().getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms) {
        for (IRoom iRoom : rooms) {
            ReservationService.getInstance().addRoom(iRoom);
        }
    }

    public void addRoom(IRoom room) {
        ReservationService.getInstance().addRoom(room);
    }

    public Collection<IRoom> getAllRooms() {
        return ReservationService.getInstance().getRoomMap().values();
    }

    public Collection<Customer> getAllCustomer() {
        return CustomerService.getInstance().getAllCustomers();
    }

    public void displayAllReservation() {
        ReservationService.getInstance().printAllReservation();
    }
}
