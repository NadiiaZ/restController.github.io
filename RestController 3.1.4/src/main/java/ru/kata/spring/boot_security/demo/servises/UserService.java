package ru.kata.spring.boot_security.demo.servises;

import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserService {
    List<User> showAllUsers();
    User showUserById(int id);
    void deleteUser(int id);
    void updateUser(int id, User user);
    void save(User user);
}
