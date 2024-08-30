package feedaggregator.controller;

import feedaggregator.module.User;
import feedaggregator.repository.UserRepository;
import feedaggregator.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TokenController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<String> getToken(@RequestBody Map<String, String> body) {
        User user = userRepository.findByEmail(body.get("email"));
        if (user == null) return ResponseEntity.notFound().build();
        if (!user.getPassword().equals(body.get("password"))) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(tokenService.createToken(user.getId()));
    }
}
