package com.springdapr.demo;

import java.time.LocalDate;
import java.time.LocalTime; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;


@RestController
public class GreetingController {

	@GetMapping("/")
	public String home() {
		LocalDate myDate = LocalDate.now();
		LocalTime mytime = LocalTime.now();
		System.out.println("got / connectionstring: " + myDate.toString() + " " + mytime.toString());
		return "hi " + myDate.toString() + " " + mytime.toString();
	}

	@PostMapping("/ping")
	public String ping() {
		LocalDate myDate = LocalDate.now();
		LocalTime mytime = LocalTime.now();
		System.out.println("got /ping connectionstring: " + myDate.toString() + " " + mytime.toString());
		return "pong " + myDate.toString() + " " + mytime.toString();
	}

	@GetMapping(value = "/dapr/subscribe", produces = "application/json")
	public Map<String, String>  daprtopics() {
		HashMap<String, String> map = new HashMap<>();
		map.put("inputtopic", "inputtopic");
		return map;
	}

	private Daprconfig getTopics() {
		return new Daprconfig();
	}
}