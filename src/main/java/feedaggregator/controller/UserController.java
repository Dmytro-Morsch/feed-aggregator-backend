package feedaggregator.controller;

import feedaggregator.dto.UserDto;
import feedaggregator.module.User;
import feedaggregator.repository.UserRepository;
import feedaggregator.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public void signup(@RequestParam String username,
                       @RequestParam String email,
                       @RequestParam String password,
                       HttpServletResponse response) throws IOException {
        userService.registerUser(username, email, password);
//        response.sendRedirect("/login");
    }
}
