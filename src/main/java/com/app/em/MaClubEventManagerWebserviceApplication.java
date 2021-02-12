package com.app.em;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@PropertySource("classpath:db-access.properties")
@PropertySource("classpath:mail-sender.properties")
public class MaClubEventManagerWebserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaClubEventManagerWebserviceApplication.class, args);
	}

}
