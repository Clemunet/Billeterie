package fr.exercice.billetterie;

import fr.exercice.billetterie.config.BilletterieProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(BilletterieProperties.class)
public class BilletterieApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilletterieApplication.class, args);
    }
}
