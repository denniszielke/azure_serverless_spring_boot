package com.springdapr.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;


@RestController
public class GreetingController {

	@GetMapping("/")
	public String home() {
		return "hi";
	}

	@GetMapping(value = "/dapr/subscribe", produces = "application/json")
	public Map<String, String>  daprtopics() {
		HashMap<String, String> map = new HashMap<>();
		map.put("key", "value");
		map.put("foo", "bar");
		map.put("aa", "bb");
		return map;
	}

	private Daprconfig getTopics() {
		return new Daprconfig();
	}
}