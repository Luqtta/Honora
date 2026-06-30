package com.honorarios.honorarios_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

// Exclui o usuario in-memory padrao do Spring (auth e via JWT) — sem senha gerada em log.
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class HonorariosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HonorariosApiApplication.class, args);
	}

}
