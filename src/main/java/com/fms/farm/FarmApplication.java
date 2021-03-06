package com.fms.farm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {
		"com.fms.farm.controller",
		"com.fms.farm.entities",
		"com.fms.farm.entities.request",
		"com.fms.farm.entities.response",
		"com.fms.farm.service",
		"com.fms.farm.util",
		"com.fms.farm.config",
		"com.fms.farm.dao",
		"com.fms.farm.service"})
public class FarmApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmApplication.class, args);
	}

}
