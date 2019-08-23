package com.example.SpirngElasProject.service.ServiceImpl;


import com.example.SpirngElasProject.model.Customer;
import com.example.SpirngElasProject.repository.CustomerRepository;
import com.example.SpirngElasProject.service.CustomerService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repository;

    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));

//    @Autowired
//    public CustomerServiceImpl(CustomerRepository repository) {
//        this.repository = repository;
//    }

    @Override
    public List<Customer> retrieveCustomers() {
        return repository.findAll().getContent();
    }

    @Override
    public Optional<Customer> retrieveCustomers(String id) {
        return repository.findById(id);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return repository.save(customer);
    }

    @Override
    public List<Customer> retrieveCustomerByName(String name) {
        return repository.findByName(name, PageRequest.of(0,1));
    }

    @Override
    public List<Customer> testQuery() {
        return repository.testQuery(Pageable.unpaged());
    }


    public JsonNode completionSuggestion(String prefix) throws IOException {

        String textForJSON = new String();

        List<String> returnList = new ArrayList<>();

        //Initialize SearchRequest
        SearchRequest searchRequest = new SearchRequest("customer");
        searchRequest.types("article");

        //Initialize searchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //Initialize CompletionSuggestionBuilder                  Initialize field and prefix
        CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders.completionSuggestion("name").prefix(prefix).size(3);

        //Initialize suggestBuilder
        SuggestBuilder suggestBuilder = new SuggestBuilder();

        //Add suggestBuilder into the tag of 'suggest'
        suggestBuilder.addSuggestion("SUGGESTION", completionSuggestionBuilder);

        searchSourceBuilder.suggest(suggestBuilder);

        //Add the SearchSourceBuilder to searchRequest
        searchRequest.source(searchSourceBuilder);

        //Initialize SearchResponse
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Suggest suggest = searchResponse.getSuggest();

        Suggest.Suggestion suggestion = suggest.getSuggestion("SUGGESTION");

        List<Suggest.Suggestion.Entry> list = suggestion.getEntries();
        for(Suggest.Suggestion.Entry entry : list) {
            List<Suggest.Suggestion.Entry.Option> options = entry.getOptions();
            for(Suggest.Suggestion.Entry.Option option : options) {
                returnList.add(option.getText().toString());
            }
        }
        textForJSON = String.valueOf(suggest);
        stringToJSON(textForJSON);


        System.out.println("------------------------------------------------------searchRequest-------------------------------------------------------------------");
        System.out.println(searchRequest);
        System.out.println("------------------------------------------------------searchResponse-------------------------------------------------------------------");
        System.out.println(searchResponse);
//        System.out.println("-----------------------------------------------------suggestBuilder--------------------------------------------------------------------");
//        System.out.println(suggestBuilder);
        System.out.println("---------------------------------------------------searchSourceBuilder-----------------------------------------------------------------");
        System.out.println(searchSourceBuilder);
        System.out.println("---------------------------------------------------------suggest-----------------------------------------------------------------");
        System.out.println(suggest);
        System.out.println("---------------------------------------------------------returnList-----------------------------------------------------------------");
        System.out.println(returnList);
//        System.out.println("---------------------------------------------------------stringToJSON-----------------------------------------------------------------");
//        System.out.println(stringToJSON(textForJSON));

        return stringToJSON(textForJSON);
    }

    public JsonNode completeSugMultiField(String prefix) throws IOException {

        List<String> returnList = new ArrayList<>();

        //Initialize SearchRequest
        SearchRequest searchRequest = new SearchRequest("library");
//        searchRequest.types("_doc");

        //Initialize searchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //Initialize CompletionSuggestionBuilder                  //Specify field and prefix
        CompletionSuggestionBuilder completionSuggestionBuilder = SuggestBuilders.completionSuggestion("name").text(prefix).size(3);
        CompletionSuggestionBuilder completionSuggestionBuilder1 = SuggestBuilders.completionSuggestion("author").text(prefix).size(3);

        //Initialize suggestBuilder
        SuggestBuilder suggestBuilder = new SuggestBuilder();

        //Add suggestBuilder into the tag of 'suggest'
        suggestBuilder.addSuggestion("NAME_SUGGESTION", completionSuggestionBuilder);
        suggestBuilder.addSuggestion("AUTHOR_SUGGESTION", completionSuggestionBuilder1);

        searchSourceBuilder.suggest(suggestBuilder);
        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));

        System.out.println("----------------------------------------------------------searchSourceBuilder---------------------------------------------------------------");
        System.out.println(searchSourceBuilder);

        //Add the SearchSourceBuilder to searchRequest
        searchRequest.source(searchSourceBuilder);

        //Initialize SearchResponse
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        Suggest suggest = searchResponse.getSuggest();

        return stringToJSON(String.valueOf(suggest));
    }

    @Override
    public JsonNode stringToJSON(String text) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(text);
        JsonNode actualObj = mapper.readTree(parser);

        return actualObj;
    }

//		Suggest suggest = searchResponse.getSuggest();
//		CompletionSuggestion suggestion = suggest.getSuggestion("SUGGESTION");
//		for (CompletionSuggestion.Entry entry : suggestion.getEntries()) {
//			for (CompletionSuggestion.Entry.Option option : entry) {
//				suggestionList.add(option.getText().toString());
//			}
//		}
}