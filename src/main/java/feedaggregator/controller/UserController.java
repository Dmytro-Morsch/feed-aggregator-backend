package feedaggregator.controller;

import feedaggregator.dto.UserDto;
import feedaggregator.module.User;
import feedaggregator.repository.UserRepository;
import feedaggregator.service.EncryptionService;
import feedaggregator.service.PasswordEncoder;
import feedaggregator.service.UserService;
import feedaggregator.util.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/api/user")
    public ResponseEntity<?> getUser(@RequestAttribute Long userId) {
        User user = userRepository.getById(userId);
        user.setEmail(encryptionService.decrypt(user.getEmail()));
        return ResponseEntity.ok(UserDto.fromEntity(user));
    }

    @PatchMapping("/api/user/update")
    public ResponseEntity<?> patchUser(@RequestBody Map<String, String> body,
                                       @RequestAttribute Long userId) {
        User user = userRepository.getById(userId);
        if (user == null) return ResponseEntity.notFound().build();

        String newEmail = body.get("email");
        String newEmailHash = Crypto.sha256(newEmail);
        if (!user.getEmailHash().equals(newEmailHash)) {
            if (!userService.userExists(user.getEmail())) {
                user.setEmail(encryptionService.encrypt(newEmail));
                user.setEmailHash(newEmailHash);
            } else {
                return ResponseEntity.status(409).build();
            }
        }

        String newPassword = body.get("password");
        if (newPassword != null) {
            if (!isPasswordValid(newPassword)) return ResponseEntity.badRequest().build();
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        user.setUsername(body.get("username"));

        userRepository.update(user);
        user.setEmail(newEmail);
        return ResponseEntity.ok(UserDto.fromEntity(user));
    }

    @PostMapping("/api/user/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> user) {
        if (userService.userExists(user.get("email"))) return ResponseEntity.status(409).build();
        if (!isPasswordValid(user.get("password"))) return ResponseEntity.badRequest().build();
        if (!user.get("repeatPassword").equals(user.get("password"))) return ResponseEntity.badRequest().build();
        userService.registerUser(user.get("username"), user.get("email"), user.get("password"));
        return ResponseEntity.ok().build();
    }

    private boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }
}
