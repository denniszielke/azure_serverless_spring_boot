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
		System.out.println("got / connectionstring: " + myDate.toString() + " " + mytime.toString());
		return "hi " + myDate.toString() + " " + mytime.toString();
	}

	@GetMapping("/hello")
	public String hello() {
		LocalDate myDate = LocalDate.now();
		LocalTime mytime = LocalTime.now();
		System.out.println("got /hello connectionstring: " + myDate.toString() + " " + mytime.toString());
		return "hello " + myDate.toString() + " " + mytime.toString();
	}
}