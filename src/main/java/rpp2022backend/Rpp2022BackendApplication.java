package rpp2022backend;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/*
 * @SpringBootApplication - anotacija se postavlja na klasi koja će se koristiti
 * za pokretanje aplikacije. Klasa koja pokreće aplikaciju mora se nalaziti u
 * osnovnom paketu. Predstavlja kobinaciju anotacija @Configuration, @EnableAutoConfiguration i
 * @ComponentScan
 * @Configuration - anotacija koja označava klasu koja definiše Spring Bean-ove. 
 * @EnableAutoConfiguration - anotacija koja označava klasu koje će kreirati Spring Bean-ove i
 * inicijalizovati različita druga podešavanja
 * @ComponentScan - anotacija koja označava gde će se tražiti klase, metode ili varijable instanci
 * koje imaju anotacije
 *  */

@SpringBootApplication
public class Rpp2022BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Rpp2022BackendApplication.class, args);
	}

	 
	 /*
		 * @Bean - koristi se na nivou metode, kako bi se stavilo do znanja da je potrebno kreirati
		 * Spring Bean. Spring Bean predstavlja objekat kojim upravlja (kreira i inicijalizuje) 
		 * Spring IoC kontejner.
		 */
	
	/*
	 * ApplicationContex - zadužen za učitavanje definicija Bean-ova, međusobno povezivanje
	 * Bean-ova i prosleđivanje Bean-ova kada su potrebni
	 */
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Beans provided by Spring Boot:");
			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName: beanNames) {
				System.out.println(beanName);
			};
		};
	}

}
