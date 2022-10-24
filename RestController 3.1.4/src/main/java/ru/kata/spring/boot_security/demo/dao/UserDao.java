package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Set;

public interface UserDao {
    List<User> showAllUsers();
    User getUserById(int id);
    User getUserByUsername(String username);
    void delete(int id);
    void update(int id, User user, Set<Role> roles);
    void save(User user);

}
