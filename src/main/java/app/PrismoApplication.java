package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication(exclude = EmbeddedMongoAutoConfiguration.class)
@EnableReactiveMongoRepositories
public class PrismoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrismoApplication.class, args);
	}

}
