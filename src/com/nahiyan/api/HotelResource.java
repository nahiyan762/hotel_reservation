package com.nahiyan.api;

import com.nahiyan.model.Customer;
import com.nahiyan.model.IRoom;
import com.nahiyan.model.Reservation;
import com.nahiyan.service.CustomerService;
import com.nahiyan.service.ReservationService;

import java.util.Collection;
import java.util.Date;

public final class HotelResource {
    private static HotelResource instance;

    private HotelResource() {
    }

    public static HotelResource getInstance() {
        if (instance == null) {
            instance = new HotelResource();
        }
        return instance;
    }

    public Customer getCustomer(String email) {
        return CustomerService.getInstance().getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        CustomerService.getInstance().addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return ReservationService.getInstance().getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        Customer customer = CustomerService.getInstance().getCustomer(customerEmail);
        return ReservationService.getInstance().reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        Customer customer = CustomerService.getInstance().getCustomer(customerEmail);
        return ReservationService.getInstance().getCustomersReservation(customer);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return ReservationService.getInstance().findRooms(checkIn, checkOut);
    }

    public void createCustomer(String email, String firstName, String lastName) {
        CustomerService.getInstance().addCustomer(email, firstName, lastName);
    }

}
