package org.example.vebproekt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class VebProektApplication {

	public static void main(String[] args) {
		SpringApplication.run(VebProektApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	} //bcrypt avtomatski i salt inkorporira

	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}


}
