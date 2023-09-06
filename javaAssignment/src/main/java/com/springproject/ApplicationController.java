package com.springproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Flux;

@Controller
public class ApplicationController {
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private AccessToken accessToken;

	@GetMapping("/user")
	public String callAPI(Model model) {
		WebClient webClient;
		String uri = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
		webClient = webClientBuilder.baseUrl(uri).build();
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri)
				.queryParam("cmd", "get_customer_list");
		//System.out.println(accessToken);
		Flux<Customer> collection = webClient.method(HttpMethod.GET)
				.uri(uriBuilder.build().toUri())
				.header("Authorization", "Bearer "+accessToken.getAccess_token())
				.retrieve()
				.bodyToFlux(Customer.class);
		 
		 model.addAttribute("people", collection.toIterable());
		 
		 return "CustomerList";
		
		/*
		 * for(Customer c : collection.toIterable()) { System.out.println(c); }
		 */
		/* return collection; */

	}


}