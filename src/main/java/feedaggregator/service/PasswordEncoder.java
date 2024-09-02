package feedaggregator.service;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PasswordEncoder {

    public String encode(String password) {
        byte[] salt = generateSalt16Byte();

        String passwordHash = encode(password, salt);
        String saltStr = Base64.getEncoder().withoutPadding().encodeToString(salt);

        return passwordHash + ":" + saltStr;
    }

    public String encode(String password, byte[] salt) {
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(2)
                .withMemoryAsKB(66536)
                .withParallelism(1)
                .withSalt(salt);

        Argon2BytesGenerator generate = new Argon2BytesGenerator();
        generate.init(builder.build());
        byte[] result = new byte[32];
        generate.generateBytes(password.getBytes(StandardCharsets.UTF_8), result, 0, result.length);
        return Base64.getEncoder().withoutPadding().encodeToString(result);
    }

    public boolean passwordsMatch(String userPassword, String passwordWithSalt) {
        String[] parts = passwordWithSalt.split(":");
        String hash = parts[0];
        byte[] salt = Base64.getDecoder().decode(parts[1]);
        return encode(userPassword, salt).equals(hash);
    }

    private byte[] generateSalt16Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        return salt;
    }
}
