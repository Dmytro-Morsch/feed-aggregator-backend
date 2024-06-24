package feedaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FeedAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedAggregatorApplication.class, args);
	}

}
