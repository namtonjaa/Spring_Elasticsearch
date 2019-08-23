package com.example.SpirngElasProject.controller;

import com.example.SpirngElasProject.model.Customer;
import com.example.SpirngElasProject.service.CustomerService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.suggest.Suggest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping()
    public ResponseEntity<?> getCustomers() {
        List<Customer> customers = customerService.retrieveCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable String id) {
        Optional<?> customer = customerService.retrieveCustomers(id);
        if(!customer.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(customer);
    }

    @PostMapping()
    public ResponseEntity<?> createCustomers(@RequestBody Customer body) {
        Customer customers = customerService.createCustomer(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(customers);
    }

    @GetMapping("/name/{exactName}")
    public ResponseEntity<?> getCustomersByName(@PathVariable String exactName) {
        List<Customer> customers = customerService.retrieveCustomerByName(exactName);
        if(customers.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/completion/{prefix}")
    public ResponseEntity<?> completeSuggestion(@PathVariable String prefix) throws IOException {
        JsonNode suggestions = customerService.completionSuggestion(prefix);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/completeMultiField/{text}")
    public ResponseEntity<?> completeSugMultiField(@PathVariable String text) throws IOException {
        JsonNode suggestions = customerService.completeSugMultiField(text);
        return ResponseEntity.ok(suggestions);
    }

}