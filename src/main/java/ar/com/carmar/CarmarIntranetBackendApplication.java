package ar.com.carmar;

import ar.com.carmar.config.ExcelProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties(ExcelProperties.class)
@EnableJpaRepositories("ar.com.carmar.repository")
@EntityScan("ar.com.carmar.entity")
public class CarmarIntranetBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarmarIntranetBackendApplication.class, args);
    }
}
