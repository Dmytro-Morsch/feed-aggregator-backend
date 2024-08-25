package feedaggregator.controller;

import feedaggregator.module.User;
import feedaggregator.repository.UserRepository;
import feedaggregator.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<String> getToken(@RequestParam String email,
                                           @RequestParam String password) {
        User user = userRepository.findByEmail(email);
        if (!user.getPassword().equals(password)) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(tokenService.createToken(user.getId()));
    }
}
