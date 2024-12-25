package com.example;

import org.springframework.boot.SpringApplication;

public class TestDemoMagicLinkApplication {

	public static void main(String[] args) {
		SpringApplication.from(DemoMagicLinkApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
