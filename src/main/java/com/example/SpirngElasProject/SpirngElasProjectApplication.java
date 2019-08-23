package com.example.SpirngElasProject;

import com.example.SpirngElasProject.model.Customer;
import com.example.SpirngElasProject.service.CustomerService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class SpirngElasProjectApplication implements CommandLineRunner {

	@Autowired
	private ElasticsearchOperations es;

	//	private RestHighLevelClient client;
	RestHighLevelClient client = new RestHighLevelClient(
			RestClient.builder(
					new HttpHost("localhost", 9200, "http"),
					new HttpHost("localhost", 9201, "http")));


	@Autowired
	private CustomerService customerService;

	public static void main(String[] args) {
		SpringApplication.run(SpirngElasProjectApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {
		getAllUser();
//		System.out.println("---------------------------------------------------getRequest----------------------------------------------------------------");
//		System.out.println(test());
//		System.out.println("---------------------------------------------------matchQuery----------------------------------------------------------------");
//		System.out.println(testTest());
//		customerService.completionSuggestion("al");
		customerService.completeSugMultiField("mist");
	}

	private void getAllUser() {
		List<Customer> customers = customerService.retrieveCustomers();
		StringBuilder stringBuilder = new StringBuilder();
		List<Customer> customers1 = customerService.retrieveCustomerByName("Aloha");
		for (Customer name: customers){
			stringBuilder.append(name).append("\n");
		}
		System.out.println("-------------------------------------------------------------getAllCustomer------------------------------------------------------");
		customers.forEach(x -> System.out.println(x));
//		System.out.println("--------------------------------------------------------------getCustomerByName-----------------------------------------------------");
//		customers1.forEach(x -> System.out.println(x));
	}

	public String test() throws IOException {

		String sourceAsString = new String();
		GetRequest getRequest = new GetRequest(
				"customer",
				"article",
				"APZGs2wBwBXSeOOLky1F");

		GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

		String index = getResponse.getIndex();
		String type = getResponse.getType();
		String id = getResponse.getId();
		if (getResponse.isExists()) {
			long version = getResponse.getVersion();
			sourceAsString = getResponse.getSourceAsString();
//			Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
//			byte[] sourceAsBytes = getResponse.getSourceAsBytes();
		} else {

		}

		return sourceAsString;
	}

	public SearchResponse testTest() throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		searchSourceBuilder.query(QueryBuilders.matchQuery("email", "aloha"));
		searchRequest.indices("customer");
		searchRequest.source(searchSourceBuilder);

		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

		return searchResponse;
	}

}
