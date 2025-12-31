package com.milklabs.playground;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.milklabs.playground.dao.CategoryDAO;
import com.milklabs.playground.dao.ProductDAO;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.lumo.Lumo;

@SpringBootApplication
@StyleSheet(Lumo.STYLESHEET) // ou  Lumo.STYLESHEET
public class PlaygroundSpringApplication implements AppShellConfigurator {

	private static final long serialVersionUID = -7228148210868622996L;

	private static final Logger log = LoggerFactory.getLogger(PlaygroundSpringApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PlaygroundSpringApplication.class, args);
	}
	
	

	@Bean
	public CommandLineRunner demoCategory(CategoryDAO repository) {
		return args -> {
			repository.findAll().forEach(category -> log.info(category.toString()));
			log.info("");
		};
	}

	@Bean
	public CommandLineRunner demoProduct(ProductDAO repository) {
		return args -> {
			repository.findAll().forEach(product -> log.info(product.toString()));
			log.info("");
		};
	}

}
