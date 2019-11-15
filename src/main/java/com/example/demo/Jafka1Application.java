package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Jafka1Application implements CommandLineRunner {
	public static void main(String[] args) {

		SpringApplication.run(Jafka1Application.class, args);
	}

	@Autowired
	private SimpleProducerForTesting sender;

	@Override
	public void run(String... strings) throws Exception {
		sender.sendProbeMessages();
	}

}
