package feedaggregator.controller;

import feedaggregator.service.TokenService;
import feedaggregator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<String> getToken(@RequestParam String email,
                                           @RequestParam String password) {
        if (!userService.isValidCredential(email, password)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(tokenService.createToken(email));
    }
}
