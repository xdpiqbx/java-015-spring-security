package cw.sprboot.dpiqb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootProjectApplication {
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(SpringBootProjectApplication.class, args);
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		System.out.println("passwordEncoder.encode(\"user_pass\") = " + passwordEncoder.encode("user_pass"));
//		System.out.println("passwordEncoder.encode(\"admin_pass\") = " + passwordEncoder.encode("admin_pass"));
	}
}
