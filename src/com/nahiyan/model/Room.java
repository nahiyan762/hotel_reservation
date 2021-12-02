package com.nahiyan.model;

public class Room implements IRoom{

    private final String roomNumber;
    private final Double roomPrice;
    private final RoomType roomType;

    public Room(String roomNumber, Double roomPrice, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return roomPrice;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public Boolean isFree() {
        return this.roomPrice == (double) 0;
    }

    @Override
    public String printRoom() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", roomPrice=" + roomPrice +
                ", roomType=" + roomType +
                '}';
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", roomPrice=" + roomPrice +
                ", roomType=" + roomType +
                '}';
    }
}
