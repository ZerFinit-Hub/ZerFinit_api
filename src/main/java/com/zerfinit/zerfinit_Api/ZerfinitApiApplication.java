package com.zerfinit.zerfinit_Api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZerfinitApiApplication {

	public static void main(String[] args) {
		// Load the .env file
		Dotenv dotenv = Dotenv.configure()
				.directory("./") // Look for .env in the project root
				.ignoreIfMissing() // Don’t fail if .env is missing
				.load();

		// Set environment variables from .env
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(ZerfinitApiApplication.class, args);
	}

}
