package com.pragma.restaurants;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsRestaurantsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsRestaurantsApplication.class, args);
	}

}
