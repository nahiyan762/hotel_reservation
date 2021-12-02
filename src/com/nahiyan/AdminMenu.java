package com.nahiyan;

import com.nahiyan.api.AdminResource;
import com.nahiyan.api.HotelResource;
import com.nahiyan.model.Customer;
import com.nahiyan.model.IRoom;
import com.nahiyan.model.Room;
import com.nahiyan.model.RoomType;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public final class AdminMenu {
    private static AdminMenu instance;

    private AdminMenu() {
    }

    public static AdminMenu getInstance() {
        if (instance == null) {
            instance = new AdminMenu();
        }
        return instance;
    }

    public void displayOptions() {
        System.out.println("Admin Menu");
        System.out.println();
        System.out.println("==========================================================");
        System.out.println("1. See all customers");
        System.out.println("2. See all rooms");
        System.out.println("3. See all reservations");
        System.out.println("4. Add a room");
        System.out.println("5. Add test data");
        System.out.println("6. Back to main menu");
        System.out.println("==========================================================");
        System.out.println("Please select a number for the menu option");
    }

    public boolean executeOption(Scanner scanner, Integer selection) {
        boolean keepAdminRunning = true;
        switch (selection) {
            case 1 -> getAllCustomers();
            case 2 -> getAllRooms();
            case 3 -> getAllReservations();
            case 4 -> addRooms(scanner);
            case 5 -> addTestData();
            case 6 -> keepAdminRunning = false;
            default -> System.out.println("Please enter a number between 1 and 6\n");
        }
        return keepAdminRunning;
    }

    private void getAllCustomers() {
        Collection<Customer> customers = AdminResource.getInstance().getAllCustomer();
        for (Customer customer : customers) {
            System.out.println(customer.toString());
        }
    }

    private void getAllRooms() {
        Collection<IRoom> rooms = AdminResource.getInstance().getAllRooms();
        for (IRoom iRoom : rooms) {
            System.out.println(iRoom.toString());
        }
    }

    private void getAllReservations() {
        AdminResource.getInstance().displayAllReservation();
    }

    private static void addRooms(Scanner scanner) {
        boolean keepAddingRooms;
        addRoom(scanner);
        do {
            System.out.println("Would you like to add another room? y/n");
            String choice = scanner.nextLine();
            try {
                if (choice.equalsIgnoreCase("y")) {
                    keepAddingRooms = false;
                    addRoom(scanner);
                } else if (choice.equalsIgnoreCase("n")) {
                    keepAddingRooms = true;
                } else {
                    throw new IllegalArgumentException("Invalid input");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
                keepAddingRooms = false;
            }
        } while (!keepAddingRooms);
    }

    private static void addRoom(Scanner scanner) {
        String roomNumber;
        boolean isRoomExist;
        do {
            System.out.println("Enter room number: ");
            roomNumber = scanner.nextLine();
            IRoom roomExists = HotelResource.getInstance().getRoom(roomNumber);
            if (roomExists == null) {
                isRoomExist = false;
            } else {
                isRoomExist = true;
                boolean isExitFromUpdate;
                do {
                    System.out.println("That room already exists. Enter u to update, or c to create, or e to exit");
                    String choice = scanner.nextLine();
                    try {
                        if (choice.equalsIgnoreCase("u")) {
                            isRoomExist = false;
                            isExitFromUpdate = true;
                        } else if (choice.equalsIgnoreCase("c")) {
                            isExitFromUpdate = true;
                        } else if (choice.equalsIgnoreCase("e")) {
                            return;
                        } else {
                            throw new IllegalArgumentException("Invalid input");
                        }
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getLocalizedMessage());
                        isExitFromUpdate = false;
                    }
                } while (!isExitFromUpdate);
            }
        } while (isRoomExist);

        // get valid price input
        double price = 0.00;
        boolean validPrice = false;
        while (!validPrice) {
            try {
                System.out.println("Enter price per night: ");
                price = Double.parseDouble(scanner.nextLine());
                if (price < 0) {
                    System.out.println("The price must be greater or equal than 0.00");
                } else {
                    validPrice = true;
                }
            } catch (Exception ex) {
                System.out.println("Please enter a valid price");
            }
        }
        // get valid room type input
        RoomType roomType = null;
        boolean validRoomType = false;
        while (!validRoomType) {
            try {
                System.out.println("Enter room type (1 for single bed, 2 for double bed): ");
                roomType = RoomType.valueForNumberOfBeds(Integer.parseInt(scanner.nextLine()));
                if (roomType == null) {
                    System.out.println("Please enter a valid room type");
                } else {
                    validRoomType = true;
                }
            } catch (Exception ex) {
                System.out.println("Please enter a valid room type");
            }
        }

        Room newRoom = new Room(roomNumber, price, roomType);
        AdminResource.getInstance().addRoom(newRoom);
    }

    private void addTestData() {
        String roomNumber;
        double price;
        RoomType roomType;
        for (int i = 1; i <= 5; i++) {
            roomNumber = 10 + Integer.toString(i);
            if (i % 2 == 0) {
                price = 200.00;
                roomType = RoomType.valueForNumberOfBeds(1);
            } else {
                price = 100.00;
                roomType = RoomType.valueForNumberOfBeds(2);
            }
            Room newRoom = new Room(roomNumber, price, roomType);
            AdminResource.getInstance().addRoom(newRoom);
        }

        HotelResource.getInstance().createCustomer("nahiyan@gmail.com", "nahiyan", "nahiyan");
        HotelResource.getInstance().createCustomer("sohaib@gmail.com", "sohaib", "sohaib");
        HotelResource.getInstance().createCustomer("rabbi@gmail.com", "rabbi", "rabbi");
        HotelResource.getInstance().createCustomer("rahid@gmail.com", "rahid", "rahid");
        HotelResource.getInstance().createCustomer("sobhan@gmail.com", "sobhan", "sobhan");

        Date today = new Date();
        Calendar calender = Calendar.getInstance();
        Date checkInDate;
        Date checkOutDate;

        calender.setTime(today);
        calender.add(Calendar.DATE, 2);
        checkInDate = calender.getTime();
        calender.setTime(checkInDate);
        calender.add(Calendar.DATE, 5);
        checkOutDate = calender.getTime();
        HotelResource.getInstance().bookARoom("nahiyan@gmail.com", HotelResource.getInstance().getRoom("101"), checkInDate, checkOutDate);

        calender.setTime(today);
        calender.add(Calendar.DATE, 4);
        checkInDate = calender.getTime();
        calender.setTime(checkInDate);
        calender.add(Calendar.DATE, 5);
        checkOutDate = calender.getTime();
        HotelResource.getInstance().bookARoom("rabbi@gmail.com", HotelResource.getInstance().getRoom("102"), checkInDate, checkOutDate);

        System.out.println("Test data added!");
    }
}
