package br.com.driw.timesheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class TimeSheet {

	public static void main(String[] args) {
		SpringApplication.run(TimeSheet.class, args);
	}

}
