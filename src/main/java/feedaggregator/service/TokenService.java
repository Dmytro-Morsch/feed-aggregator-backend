package feedaggregator.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Date;

@Service
public class TokenService {

    public String createToken(Long userId) {
        try {
            JWSSigner signer = new MACSigner(tokenSignKey());
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userId.toString())
                    .expirationTime(new Date(new Date().getTime() + 5 * 60 * 1000))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (IOException | JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            long userId = Long.parseLong(signedJWT.getJWTClaimsSet().getSubject());
            JWSVerifier verifier = new MACVerifier(tokenSignKey());
            return signedJWT.verify(verifier) ? userId : null;
        } catch (ParseException | JOSEException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    private byte[] tokenSignKey() throws IOException {
        var path = Path.of("jwt.key");
        byte[] key;
        if (!Files.exists(path)) {
            key = new byte[32];
            new SecureRandom().nextBytes(key);
            Files.write(path, key);
        } else {
            key = Files.readAllBytes(path);
        }
        return key;
    }
}
