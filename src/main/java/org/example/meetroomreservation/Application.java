package org.example.meetroomreservation;
import org.example.meetroomreservation.domain.User;
import org.example.meetroomreservation.repos.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
