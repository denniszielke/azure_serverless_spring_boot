package com.springazuremessaging.demo;

import java.time.LocalDate;
import java.time.LocalTime; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {

	@GetMapping("/")
	public String home() {
		LocalDate myDate = LocalDate.now();
		LocalTime mytime = LocalTime.now();
		return "hi " + myDate.toString() + " " + mytime.toString();
	}
}