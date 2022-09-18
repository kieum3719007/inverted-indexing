package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class JspIndexingApplication {
	public static void main(String[] args) {
		SpringApplication.run(
				JspIndexingApplication.class,
				args
				);
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		final CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("UTF-8");
		resolver.setResolveLazily(true);
		return resolver;
	}
}
