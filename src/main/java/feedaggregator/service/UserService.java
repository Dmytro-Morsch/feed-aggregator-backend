package feedaggregator.service;

import feedaggregator.module.User;
import feedaggregator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void registerUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
    }

    public boolean isValidCredential(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user.getPassword().equals(password);
    }

    public boolean userExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }
}
