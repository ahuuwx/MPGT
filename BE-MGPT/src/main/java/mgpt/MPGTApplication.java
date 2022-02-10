package mgpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@EnableScheduling
@SpringBootApplication
public class MPGTApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(MPGTApplication.class, args);
    }

}
