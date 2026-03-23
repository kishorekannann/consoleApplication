package repository;

import data.DataStore;
import model.Role;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {
    public User findById(int id) {
        return DataStore.users.get(id);
    }

    public User findByEmail(String email) {
        return DataStore.users.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    public List<User> findByRole(Role role) {
        return DataStore.users.values().stream()
                .filter(u -> u.getRole() == role)
                .collect(Collectors.toList());
    }

    public List<User> findAll() {
        return new ArrayList<>(DataStore.users.values());
    }

    public void save(User user) {
        DataStore.users.put(user.getId(), user);
    }
}
