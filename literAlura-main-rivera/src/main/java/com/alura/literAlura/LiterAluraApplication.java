package com.alura.literAlura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alura.literAlura.principal.Principal;
import com.alura.literAlura.repository.AutorRepository;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	@Autowired
	private AutorRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.mostrarMenu();
	}

}