package com.example.rotinaAPP;

import com.example.rotinaAPP.Config.DatabaseUrlConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RotinaAppApplication {

	public static void main(String[] args) {
		// Converte DATABASE_URL (formato postgres://) para JDBC antes de subir o contexto.
		DatabaseUrlConfigurer.apply();
		SpringApplication.run(RotinaAppApplication.class, args);
	}
}
