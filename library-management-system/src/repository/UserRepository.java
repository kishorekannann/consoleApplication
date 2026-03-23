package repository;

import data.DataStore;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    public User findByEmail(String email) {
        for (User u : DataStore.users.values()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public User findById(int id) {
        return DataStore.users.get(id);
    }

    public void save(User user) {
        DataStore.users.put(user.getId(), user);
    }

    public List<User> findAll() {
        return new ArrayList<>(DataStore.users.values());
    }
}
