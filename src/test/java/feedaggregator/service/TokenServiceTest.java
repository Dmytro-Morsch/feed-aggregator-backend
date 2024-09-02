package feedaggregator.service;

import feedaggregator.module.User;
import feedaggregator.repository.UserRepository;
import feedaggregator.util.Crypto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class TokenServiceTest {
    @Autowired
    private UserRepository userRepository;

    private final TokenService tokenService = new TokenService();

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("Test");
        user.setEmail("test@example.com");
        user.setEmailHash(Crypto.sha256("test@example.com"));
        user.setPassword("secret");
        userRepository.save(user);
    }

    @Test
    void testGetUserIdFromToken() {
        String token = tokenService.createToken(user.getId());

        Long userId = tokenService.getUserIdFromToken(token);

        assertEquals(user.getId(), userId);
    }
}