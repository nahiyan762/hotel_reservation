package com.nahiyan;


import com.nahiyan.api.HotelResource;
import com.nahiyan.model.Customer;
import com.nahiyan.model.IRoom;
import com.nahiyan.model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public final class MainMenu {

    private static MainMenu instance;

    private MainMenu() {
    }

    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    public void displayOptions() {
        System.out.println("Welcome to the Hotel Reservation Application");
        System.out.println();
        System.out.println("==========================================================");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an Account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("==========================================================");
        System.out.println("Please select a number for the menu option");
    }

    public boolean executeOption(Scanner scanner, Integer selection) {
        boolean keepRunning = true;
        switch (selection) {
            case 1 -> findAndReserveRoom(scanner);
            case 2 -> getCustomerReservations(scanner);
            case 3 -> createAccount(scanner);
            case 4 -> runAdminMenu(scanner);
            case 5 -> keepRunning = false;
            default -> System.out.println("Please enter a number between 1 and 5\n");
        }
        return keepRunning;
    }

    private void getCustomerReservations(Scanner scanner) {
        System.out.println("Please enter your Email (format: name@domain.com): ");
        String email = scanner.nextLine();
        Customer customer = HotelResource.getInstance().getCustomer(email);
        if (customer == null) {
            System.out.println("Customer not found");
            return;
        }
        Collection<Reservation> reservations = HotelResource.getInstance().getCustomersReservations(customer.getEmail());
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("You don't have any reservation");
            return;
        }
        for (Reservation reservation : reservations) {
            System.out.println(reservation.toString());
        }
    }

    private void runAdminMenu(Scanner scanner) {
        boolean keepRunning = true;
        try {
            while (keepRunning) {
                try {
                    AdminMenu.getInstance().displayOptions();
                    int selection = Integer.parseInt(scanner.nextLine());
                    keepRunning = AdminMenu.getInstance().executeOption(scanner, selection);
                } catch (Exception ex) {
                    System.out.println("Please enter a number between 1 and 5\n");
                }
            }
        } catch (Exception ex) {
            System.out.println("\nError - Exiting program...\n");
        }
    }

    private String createAccount(Scanner scanner) {
        String email = null;
        System.out.println("First name: ");
        String firstName = scanner.nextLine();
        System.out.println("Last name: ");
        String lastName = scanner.nextLine();
        boolean validEmail = false;
        while (!validEmail) {
            System.out.println("Enter Email format: name@example.com");
            email = scanner.nextLine();
            try {
                HotelResource.getInstance().createACustomer(email, firstName, lastName);
                System.out.println("Account created successfully!\n");
                validEmail = true;
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
        return email;
    }

    private void findAndReserveRoom(Scanner scanner) {
        Date checkInDate;
        Date checkOutDate;
        Collection<IRoom> availableRooms;
        boolean keepWantToBookRoom;
        boolean isWantToBookRoom = false;
        do {
            checkInDate = getValidCheckInDate(scanner);
            checkOutDate = getValidCheckOutDate(scanner, checkInDate);
            availableRooms = HotelResource.getInstance().findARoom(checkInDate, checkOutDate);
            if (availableRooms == null) {
                System.out.println("No Rooms created");
                return;
            } else if (!availableRooms.isEmpty()) {
                System.out.println("Available rooms for CheckIn on " + checkInDate + " and CheckOut on " + checkOutDate);
                for (IRoom room : availableRooms) {
                    System.out.println(room.printRoom());
                }
                System.out.println("Would you like to book a room? Enter y for yes, or any other character for no");
                String choice = scanner.nextLine();
                isWantToBookRoom = choice.equalsIgnoreCase("y");
                keepWantToBookRoom = false;
            } else {
                Date newCheckInDate = getRecommendedDate(checkInDate);
                Date newCheckOutDate = getRecommendedDate(checkOutDate);
                availableRooms = HotelResource.getInstance().findARoom(newCheckInDate, newCheckOutDate);
                keepWantToBookRoom = true;
                if (!availableRooms.isEmpty()) {
                    System.out.println("There are no available rooms for those dates. Rooms available for alternative dates, check-in on " + newCheckInDate + " and check-out on " + newCheckOutDate);
                    for (IRoom room : availableRooms) {
                        System.out.println(room.printRoom());
                        System.out.println();
                    }
                } else {
                    System.out.println("There are no available rooms for those dates");
                }
            }
        } while(keepWantToBookRoom);


        boolean haveAccount;
        if (isWantToBookRoom) {
            System.out.println("Do you have an account? Enter y for yes, or any other character for no");
            String choice = scanner.nextLine();
            haveAccount = choice.equalsIgnoreCase("y");
        } else {
            System.out.println("Available rooms not found on this days");
            return;
        }

        String email;
        if (haveAccount) {
            System.out.println("Please enter your Email example name@domain.com");
            email = scanner.nextLine();
        } else {
            email = createAccount(scanner);
        }

        Customer customer = HotelResource.getInstance().getCustomer(email);
        if (customer == null) {
            System.out.println("Sorry, no account exists for that email");
            return;
        }

        IRoom room = getRoomForReservation(scanner, availableRooms);
        Reservation reservation = HotelResource.getInstance().bookARoom(customer.getEmail(), room, checkInDate, checkOutDate);
        if (reservation == null) {
            System.out.println("Couldn't process your booking, the room is not available");
        } else {
            System.out.println("Thank you! Your room was booked successfully!");
            System.out.println(reservation);
        }
    }

    private Date getValidCheckInDate(Scanner scanner) {
        SimpleDateFormat DateFor = new SimpleDateFormat("MM/dd/yyyy");
        Date checkInDate = null;
        boolean isValidCheckInDate = false;
        while (!isValidCheckInDate) {
            System.out.println("Enter CheckIn Date month/date/year example 02/21/2021");
            String inputCheckInDate = scanner.nextLine();
            try {
                checkInDate = DateFor.parse(inputCheckInDate);
                Date today = new Date();
                if (checkInDate.before(today)) {
                    System.out.println("CheckIn date cannot be in the past");
                } else {
                    isValidCheckInDate = true;
                }
            } catch (ParseException ex) {
                System.out.println("Invalid date format, please use month/date/year example 02/21/2021");
            }
        }
        return checkInDate;
    }

    private Date getValidCheckOutDate(Scanner scanner, Date checkInDate) {
        SimpleDateFormat DateFor = new SimpleDateFormat("MM/dd/yyyy");
        Date checkOutDate = null;
        boolean isValidCheckOutDate = false;
        while (!isValidCheckOutDate) {
            System.out.println("Enter CheckOut Date month/date/year example 02/21/2021");
            String inputCheckOutDate = scanner.nextLine();
            try {
                checkOutDate = DateFor.parse(inputCheckOutDate);
                if (checkOutDate.before(checkInDate)) {
                    System.out.println("CheckOut date can't be before the CheckIn date");
                } else {
                    isValidCheckOutDate = true;
                }
            } catch (ParseException ex) {
                System.out.println("Invalid date format, please use month/date/year example 02/21/2021");
            }
        }
        return checkOutDate;
    }

    private IRoom getRoomForReservation(Scanner scanner, Collection<IRoom> availableRooms) {
        IRoom room = null;
        String roomNumber = null;
        boolean validRoomNumber = false;
        while (!validRoomNumber) {
            System.out.println("Enter the room number: ");
            roomNumber = scanner.nextLine();
            room = HotelResource.getInstance().getRoom(roomNumber);
            if (room == null) {
                System.out.println("That room doesn't exists, please enter a valid room number");
            } else {
                if (!availableRooms.contains(room)) {
                    System.out.println("That room is not available, please enter a valid room number");
                } else {
                    validRoomNumber = true;
                }
            }
        }
        return room;
    }

    private Date getRecommendedDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 7);
        return c.getTime();
    }
}
