package feedaggregator.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testUserExists() {
        userService.registerUser("Test", "test@example.com", "secret");

        assertTrue(userService.userExists("test@example.com"));
        assertFalse(userService.userExists("wrongEmail@example.com"));
    }
}