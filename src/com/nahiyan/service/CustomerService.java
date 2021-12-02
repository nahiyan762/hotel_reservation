package com.nahiyan.service;

import com.nahiyan.model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class CustomerService {

    private static CustomerService instance;

    private CustomerService() {
    }

    private final Map<String, Customer> customerMap = new HashMap<>();

    public static CustomerService getInstance(){
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }

    public void addCustomer(String email, String firstName, String lastName){
        Customer customer = new Customer(firstName, lastName, email);
        customerMap.put(customer.getEmail(), customer);
    }

    public Customer getCustomer(String email){
        return customerMap.get(email);
    }

    public Collection<Customer> getAllCustomers() {
        return customerMap.values();
    }


}
