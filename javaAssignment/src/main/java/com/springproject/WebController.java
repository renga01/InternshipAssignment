package com.springproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
