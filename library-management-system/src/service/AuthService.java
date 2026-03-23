package service;

import model.User;
import repository.UserRepository;

public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    public User login(String email, String password) {
        User u = userRepository.findByEmail(email);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }
}
