package ar.com.carmar;

import ar.com.carmar.config.ExcelProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExcelProperties.class)
public class CarmarIntranetBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarmarIntranetBackendApplication.class, args);
    }
}
