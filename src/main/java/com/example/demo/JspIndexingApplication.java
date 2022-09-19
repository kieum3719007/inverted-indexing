package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class JspIndexingApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(
				JspIndexingApplication.class,
				args
				);
	}

	private final MongoTemplate mongoTemplate;

	public JspIndexingApplication(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		final CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("UTF-8");
		resolver.setResolveLazily(true);
		return resolver;
	}

	@Override
	public void run(String... args) throws Exception {
		//		final Set<String> collectionNames = mongoTemplate.getCollectionNames();
		//		collectionNames.forEach(name -> mongoTemplate.dropCollection(name));
	}
}
