package feedaggregator.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class EncryptionService {
    private static final int TAG_LENGTH_BITS = 128;
    private static final int IV_LENGTH_BYTES = 12;
    private static final String CIPHER = "AES/GCM/NoPadding";

    private final SecureRandom secureRandom = new SecureRandom();
    private final SecretKey secretKey;

    public EncryptionService(byte[] key) {
        secretKey = new SecretKeySpec(key, "AES");
    }

    public String encrypt(String input) {
        byte[] inputBytes = input.getBytes(UTF_8);
        byte[] cipherBytes = encrypt(inputBytes);
        return Base64.getEncoder().withoutPadding().encodeToString(cipherBytes);
    }

    public String decrypt(String input) {
        byte[] cipherBytes = Base64.getDecoder().decode(input.getBytes());
        byte[] outputBytes = decrypt(cipherBytes);
        return new String(outputBytes, UTF_8);
    }

    private byte[] encrypt(byte[] input) {
        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            secureRandom.nextBytes(iv);

            var paramSpec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
            var cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] encryptedBytes = cipher.doFinal(input);

            var outputBuf = ByteBuffer.allocate(IV_LENGTH_BYTES + encryptedBytes.length);
            outputBuf.put(iv);
            outputBuf.put(encryptedBytes);
            return outputBuf.array();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] decrypt(byte[] input) {
        try {
            var byteBuffer = ByteBuffer.wrap(input);
            byte[] iv = new byte[IV_LENGTH_BYTES];
            byteBuffer.get(iv);
            byte[] cipherBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherBytes);
            var cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            return cipher.doFinal(cipherBytes);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
