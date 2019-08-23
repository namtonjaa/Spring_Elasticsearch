package com.example.SpirngElasProject.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import javax.validation.constraints.NotNull;

@Data
@Document(indexName = "customer", type = "article")
public class Customer {

    @Id
    private String id;

    @NotNull
    private String name;

    @NotNull
    private Integer age;

    @NotNull
    private String email;
}