package feedaggregator.service;

import feedaggregator.module.User;
import feedaggregator.repository.UserRepository;
import feedaggregator.util.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EncryptionService encryptionService;

    public void registerUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(encryptionService.encrypt(email));
        user.setEmailHash(Crypto.sha256(email));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public boolean userExists(String email) {
        User user = userRepository.findByEmailHash(Crypto.sha256(email));
        return user != null;
    }
}
