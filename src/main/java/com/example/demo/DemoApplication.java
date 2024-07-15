package com.example.demo;

import com.example.demo.rest.*;
import com.example.demo.rest.entities.Dog;
import com.example.demo.rest.entities.Human;
import com.example.demo.rest.entities.Passport;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(AnimalRepository repository, PassportRepository fileRepository) {
		return args -> {
			var dog = Dog.builder()
					.name("Jake the Dog")
					.isChipped(false)
					.dna(new byte[] {4, 5, 6})
					.build();
			var human = Human.builder()
					.name("Finn the Human")
					.dna(new byte[]{1, 2, 3})
					.build();
			repository.save(human);
			var currentPassport = Passport.builder()
					.country("Canada").owner(human)
					.expirationYear(2027)
					.issueYear(2017).build();
			fileRepository.save(currentPassport);
			var oldPassport = Passport.builder()
					.country("Canada").owner(human)
					.expirationYear(2004)
					.issueYear(2009).build();
			fileRepository.save(oldPassport);
			repository.save(dog);
		};
	}
}
