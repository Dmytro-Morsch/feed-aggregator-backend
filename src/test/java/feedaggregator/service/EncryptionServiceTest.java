package feedaggregator.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EncryptionServiceTest {
    private final EncryptionService encryptionService = new EncryptionService(new byte[32]);

    @Test
    void testTextIsEncrypted() {
        String plainText = "Test input";

        String encrypted = encryptionService.encrypt(plainText);

        assertNotEquals(plainText, encrypted);
    }

    @Test
    void testTextIsDecrypted() {
        String encrypted = "9uEdeTpqubHvr/iteI8UeNnDZ6SKrwyRbgnNp/ehqzumH+83qwE";

        String decrypted = encryptionService.decrypt(encrypted);

        assertNotEquals(encrypted, decrypted);
        assertEquals("Test input", decrypted);
    }
}