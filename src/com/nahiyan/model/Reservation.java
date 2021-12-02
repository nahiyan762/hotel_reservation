package com.nahiyan.model;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
    private final Customer customer;
    private final IRoom iRoom;
    private final Date checkInDate;
    private final Date checkOutDate;

    public Reservation(Customer customer, IRoom iRoom, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.iRoom = iRoom;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public IRoom getIRoom() {
        return iRoom;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public boolean isRoomReserved(Date checkInDate, Date checkOutDate) {
        return checkInDate.before(this.checkOutDate) && checkOutDate.after(this.checkInDate);
    }

    public IRoom getRoom() {
        return this.iRoom;
    }

    @Override
    public String toString() {
        Format formatter = new SimpleDateFormat("MM/dd/yyyy");
        String checkIn = formatter.format(checkInDate);
        String checkOut = formatter.format(checkOutDate);
        return "Reservation{" +
                "customer=" + customer +
                ", iRoom=" + iRoom +
                ", checkInDate=" + checkIn +
                ", checkOutDate=" + checkOut +
                '}';
    }
}
