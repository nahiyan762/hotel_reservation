package com.nahiyan.model;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;

    public Customer(String firstName, String lastName, String email) throws IllegalArgumentException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = getValidEmail(email);
    }

    private String getValidEmail(String email) {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (email.equals("")){
            email = firstName+"@"+lastName+".com";
        }

        if (pattern.matcher(email).matches()) {
            return email;
        } else {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return getFirstName().equals(customer.getFirstName()) && getLastName().equals(customer.getLastName()) && getEmail().equals(customer.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getEmail());
    }
}
