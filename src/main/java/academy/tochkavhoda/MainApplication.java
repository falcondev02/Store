package academy.tochkavhoda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {
    public static void main(final String[] args) {
        System.out.println("Start application");
        SpringApplication.run(MainApplication.class);
    }
}