package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import com.mongodb.ConnectionString;

@SpringBootApplication
public class DemoApplication {

	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public MongoDatabaseFactory mongoDatabaseFactory() {
		System.out.println("==========================================");
		System.out.println("Creating MongoClient with URI: " + mongoUri);
		System.out.println("==========================================");
		return new SimpleMongoClientDatabaseFactory(new ConnectionString(mongoUri));
	}

	@Bean
	public CommandLineRunner logMongoUri() {
		return args -> {
			System.out.println("==========================================");
			System.out.println("MongoDB URI from Spring Boot: " + mongoUri);
			System.out.println("==========================================");
		};
	}

}
