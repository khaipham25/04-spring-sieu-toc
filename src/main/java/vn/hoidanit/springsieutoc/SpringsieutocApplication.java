package vn.hoidanit.springsieutoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})

public class SpringsieutocApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringsieutocApplication.class, args);
	}

}
