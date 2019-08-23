package com.example.SpirngElasProject.repository;

import com.example.SpirngElasProject.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;

@EnableElasticsearchRepositories
public interface CustomerRepository extends ElasticsearchRepository<Customer, String> {

    Page<Customer> findAll();

    List<Customer> findByName(String name, Pageable pageable);

    @Query("{\n" +
            "  \"query\": {\n" +
            "    \"match\": {\n" +
            "      \"email\": \"aloha\"\n" +
            "    }\n" +
            "  }\n" +
            "}")
    List<Customer> testQuery(Pageable pageable);
}