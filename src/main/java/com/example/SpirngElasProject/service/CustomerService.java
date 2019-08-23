package com.example.SpirngElasProject.service;

import com.example.SpirngElasProject.model.Customer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> retrieveCustomers();
    Optional<Customer> retrieveCustomers(String id);
    Customer createCustomer(Customer customer);
    List<Customer> retrieveCustomerByName(String Name);
    List<Customer> testQuery();
    JsonNode completionSuggestion(String prefix) throws IOException;
    JsonNode stringToJSON(String text) throws IOException;
    JsonNode completeSugMultiField(String prefix) throws IOException;
}
