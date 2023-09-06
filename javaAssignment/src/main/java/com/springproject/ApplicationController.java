package com.springproject;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Flux;

@RestController
public class ApplicationController {
	private final WebClient webClient;
	private String uri = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";

	public ApplicationController(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl(uri).build();
	}


	@GetMapping("/user")
	public Flux<Customer> callAPI() {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri)
	            .queryParam("cmd", "get_customer_list");
		return webClient.method(HttpMethod.GET)
				.uri(uriBuilder.build().toUri())
	            .header("Authorization", "Bearer dGVzdEBzdW5iYXNlZGF0YS5jb206VGVzdEAxMjM=")
	            .retrieve()
	            .bodyToFlux(Customer.class);
	             
	}


}