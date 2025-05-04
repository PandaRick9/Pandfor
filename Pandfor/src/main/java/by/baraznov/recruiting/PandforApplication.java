package by.baraznov.recruiting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PandforApplication {

    public static void main(String[] args) {
        SpringApplication.run(PandforApplication.class, args);
    }

}
