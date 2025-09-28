package PFE.PFE_SI.Service;

import PFE.PFE_SI.Model.User;
import PFE.PFE_SI.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //  Get all
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    //  Get by ID
    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    //  Get by nom
    public User getUserByLastname(String lastname) {
        return userRepository.findUserByLastname(lastname);
    }

    //  Get by email (important pour Spring Security)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    //  Create (avec encodage du mot de passe)
    public User createUser(User user) {
        // encoder le mot de passe avant deregister
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Delete
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    // Update
    public User updateUser(String id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }


}
