package com.springproject;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ApiService {
	
	private final WebClient webClient;
	private String uri = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";

	public ApiService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl(uri).build();
	}


	
	public String callAPI() {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri)
	            .queryParam("cmd", "get_customer_list");
		return webClient.method(HttpMethod.GET)
				.uri(uriBuilder.build().toUri())
	            .header("Authorization", "Bearer dGVzdEBzdW5iYXNlZGF0YS5jb206VGVzdEAxMjM=")
	            .retrieve()
	            .bodyToMono(String.class)
	            .block(); 
		

	}

}
