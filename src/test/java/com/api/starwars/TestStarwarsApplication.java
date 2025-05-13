package com.api.starwars;

import org.springframework.boot.SpringApplication;

public class TestStarwarsApplication {

	public static void main(String[] args) {
		SpringApplication.from(StarwarsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
