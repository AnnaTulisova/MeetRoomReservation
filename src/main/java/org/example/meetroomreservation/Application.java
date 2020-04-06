package org.example.meetroomreservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

   /* @Bean
    CommandLineRunner runner(UserRepository userRepository) {
        return args -> {
            userRepository.save(new User("a.tulisova@mail.ru", new BCryptPasswordEncoder().encode("120200")));
        };
    }*/
}
