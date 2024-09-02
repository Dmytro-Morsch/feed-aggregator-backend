package feedaggregator.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new PasswordEncoder();

    @Test
    void testEncodedPassword() {
        String userPassword = "secret";

        String encodedPassword = passwordEncoder.encode(userPassword);

        assertNotEquals(userPassword, encodedPassword);
    }

    @Test
    void testPasswordsMatch() {
        String userPassword = "secret";
        String passwordWithSalt = passwordEncoder.encode(userPassword);

        assertTrue(passwordEncoder.passwordsMatch(userPassword, passwordWithSalt));
        assertFalse(passwordEncoder.passwordsMatch("wrongPassword", passwordWithSalt));
    }
}