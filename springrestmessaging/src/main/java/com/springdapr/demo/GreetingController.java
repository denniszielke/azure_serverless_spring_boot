package com.springdapr.demo;

import java.time.LocalDate;
import java.time.LocalTime; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;


@RestController
public class GreetingController {

	static final String SB_CONNECTIONSTRING = "SB_CONNECTIONSTRING";
	static final String SB_CONNECTIONSTRING_VAULT = "SB_CONNECTIONSTRING_SECURE";

	@GetMapping("/")
	public String home() {
		LocalDate myDate = LocalDate.now();
		LocalTime mytime = LocalTime.now();
		System.out.println("got / connectionstring: " + myDate.toString() + " " + mytime.toString());
		return "hi " + myDate.toString() + " " + mytime.toString();
	}

	@GetMapping("/secure")
	public String secure() {
		String env = System.getenv(SB_CONNECTIONSTRING);
		if (env != null) {
			System.out.println("Found connectionstring: " + env);
		}
		String vault = System.getenv(SB_CONNECTIONSTRING_VAULT );
		if (vault != null) {
			System.out.println("Found vault connectionstring: " + vault);
		}
		LocalDate myDate = LocalDate.now();
		LocalTime mytime = LocalTime.now();
		System.out.println("got /secure connectionstring: " + myDate.toString() + " " + mytime.toString());
		return "hi " + myDate.toString() + " " + mytime.toString() + " env: " + env + " secure :" + vault;
	}

	@PostMapping(value= "/inputtopic", produces = "application/json")
	@ResponseBody
	public String inputtopic(@RequestBody String raw) {
		LocalDate myDate = LocalDate.now();
		LocalTime mytime = LocalTime.now();
		System.out.println("got /inputtopic connectionstring: " + myDate.toString() + " " + mytime.toString() + " body: " + raw);
		return "inputtopic " + myDate.toString() + " " + mytime.toString() + " body " + raw;
	}

	@PostMapping(value= "/outputtopic", produces = "application/json")
	@ResponseBody
	public String outputtopic(@RequestBody String raw) {
		LocalDate myDate = LocalDate.now();
		LocalTime mytime = LocalTime.now();
		System.out.println("got /outputtopic connectionstring: " + myDate.toString() + " " + mytime.toString() + " body: " + raw);
		return "outputtopic " + myDate.toString() + " " + mytime.toString() + " body " + raw;
	}

	@GetMapping(value = "/dapr/subscribe", produces = "application/json")
	public String daprtopics() {
		String json = "[{ \"topic\": \"outputtopic\", \"route\": \"outputtopic\" }]";
		return json;
	}
}


	

