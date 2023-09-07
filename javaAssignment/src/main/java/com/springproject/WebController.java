package com.springproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class WebController {

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Autowired
	private AccessToken accessToken;

	@GetMapping(value = "/login")
	public String showForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "login";
	}
	@PostMapping(value = "/login")
	public RedirectView submitForm(@ModelAttribute User user) {


		WebClient webClient = webClientBuilder.baseUrl("https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp").build();



		ResponseEntity<String> response = webClient .post() // The API endpoint path
				.contentType(MediaType.APPLICATION_JSON) 
				.body(BodyInserters.fromValue(user))
				.retrieve()
				.toEntity(String.class)
				.block();

		//System.out.println(response.getBody());

		String json = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			accessToken.setAccess_token(objectMapper.readValue(json, AccessToken.class).getAccess_token());
			//System.out.println(accessToken);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/user");
		return redirectView;

	}
	@PostMapping("/create")
	String ShowForm(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer", customer);
		return "CreateUser";
	}
	@PostMapping("/created")
	public RedirectView submit(@ModelAttribute Customer customer)
	{
		String uri = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
		WebClient webClient = webClientBuilder.baseUrl(uri).build();
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri)
				.queryParam("cmd", "create");

		ResponseEntity<String> response = webClient .post()
				.uri(uriBuilder.build().toUri())
				.header("Authorization", "Bearer "+accessToken.getAccess_token())
				.contentType(MediaType.APPLICATION_JSON) 
				.body(BodyInserters.fromValue(customer))
				.retrieve()
				.toEntity(String.class)
				.block();

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/user");
		return redirectView;

	}
	@PostMapping("/delete")
	RedirectView Form(
			@RequestParam(value = "UUID", required = false)String UUID) {
		
		String uri = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
		WebClient webClient = webClientBuilder.baseUrl(uri).build();

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri)
				.queryParam("cmd", "delete")
				.queryParam("uuid", UUID);
		//	System.out.println(UUID);

		String response = webClient .post()
				.uri(uriBuilder.build().toUri())
				.header("Authorization", "Bearer "+accessToken.getAccess_token())
				.retrieve()
				.bodyToMono(String.class)
				.block();



		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("/user");
		return redirectView;
	}
	@PostMapping("/update")
	String update(@RequestParam(value = "UUID", required = false)String UUID,Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer", customer);
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

		for(Customer c : collection.toIterable()) { 
			String uuid = c.getUUID();
			if(uuid.equals(UUID)) {
				model.addAttribute("person", c);	
			}
		}
		return "UpdateUser";
	}
	@PostMapping("/updated")
	public RedirectView updated(@ModelAttribute Customer customer)
	{  
		String uri = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
		WebClient webClient = webClientBuilder.baseUrl(uri).build();
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri)
				.queryParam("cmd", "update")
				.queryParam("uuid", customer.getUUID());

		ResponseEntity<String> response = webClient .post()
				.uri(uriBuilder.build().toUri())
				.header("Authorization", "Bearer "+accessToken.getAccess_token())
				.contentType(MediaType.APPLICATION_JSON) 
				.body(BodyInserters.fromValue(customer))
				.retrieve()
				.toEntity(String.class)
				.block();
		
		
		  RedirectView redirectView = new RedirectView(); redirectView.setUrl("/user");
		  return redirectView;
		 
	}
	@PostMapping("/error")
	public String error() {
		return "Error";
	}
	/* Working Code
	 * 
	 * 	@PostMapping(value = "/login")
	public ResponseEntity<String> submitForm(@ModelAttribute User user) {


        WebClient webClient = webClientBuilder.baseUrl("https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp").build();



		 ResponseEntity<String> response = webClient .post() // The API endpoint path
		 .contentType(MediaType.APPLICATION_JSON) 
		 .body(BodyInserters.fromValue(user))
		 .retrieve()
		 .toEntity(String.class)
		 .block();

		 System.out.println(response.getBody());

		 String json = response.getBody();
		 ObjectMapper objectMapper = new ObjectMapper();
		 try {
			accessToken.setAccess_token(objectMapper.readValue(json, AccessToken.class).getAccess_token());
			System.out.println(accessToken);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 return response;

	}
	 * 
	 * 
	 * 
	 * 
	 */
}
