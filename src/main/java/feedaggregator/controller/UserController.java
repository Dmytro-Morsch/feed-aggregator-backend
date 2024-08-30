package feedaggregator.controller;

import feedaggregator.dto.UserDto;
import feedaggregator.module.User;
import feedaggregator.repository.UserRepository;
import feedaggregator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/api/user")
    public ResponseEntity<?> getUser(@RequestAttribute Long userId) {
        User user = userRepository.getById(userId);
        return ResponseEntity.ok(UserDto.fromEntity(user));
    }

    @PostMapping("/api/user/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> user) {
        if (userService.userExists(user.get("email"))) return ResponseEntity.status(409).build();
        userService.registerUser(user.get("username"), user.get("email"), user.get("password"));
        return ResponseEntity.ok().build();
    }
}
