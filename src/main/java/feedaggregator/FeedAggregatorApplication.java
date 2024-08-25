package feedaggregator;

import feedaggregator.filter.TokenFilter;
import feedaggregator.service.EncryptionService;
import feedaggregator.service.TokenService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;

@SpringBootApplication
@EnableScheduling
public class FeedAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedAggregatorApplication.class, args);
	}

	@Bean
	public EncryptionService encryptionService() throws IOException {
		var path = Path.of("dbencrypt.key");
		byte[] key;
		if (!Files.exists(path)) {
			key = new byte[32];
			new SecureRandom().nextBytes(key);
			Files.write(path, key);
		} else {
			key = Files.readAllBytes(path);
		}
		return new EncryptionService(key);
	}

	@Bean
	public FilterRegistrationBean<?> tokenFilterRegistrationBean(TokenService tokenService) {
		FilterRegistrationBean<TokenFilter> registrationBean = new FilterRegistrationBean<>(new TokenFilter(tokenService));
		registrationBean.addUrlPatterns("/api/*");
		return registrationBean;
	}
}
