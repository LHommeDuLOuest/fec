package fr.bzh.rzh.run;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author KHERBICHE L
 */

@SpringBootApplication
@ComponentScan({"fr.bzh.rzh.web.conf"})
public class Launch {
	
	public static void main(String[] args) {
		SpringApplication.run(Launch.class, args);
	}
	
	 @Bean
	 public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
	        return args -> {

	            System.out.println("Let's inspect the beans provided by Spring Boot:");

	            String[] beanNames = ctx.getBeanDefinitionNames();
	            Arrays.sort(beanNames);
	            for (String beanName : beanNames) {
	                System.out.println(beanName);
	            }

	        };
	    }

}
