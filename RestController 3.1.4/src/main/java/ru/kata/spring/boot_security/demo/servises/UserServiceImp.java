package ru.kata.spring.boot_security.demo.servises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDaoImp;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UsersRepository;
import ru.kata.spring.boot_security.demo.security.UserDetailsImp;
import ru.kata.spring.boot_security.demo.utils.UserNotFoundException;

import java.util.*;

@Service
public class UserServiceImp implements UserService, UserDetailsService {
    private final UsersRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UsersRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          UserDaoImp userDao) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found!");
        return new UserDetailsImp(user.get());
    }

    @Override
    public List<User> showAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User showUserById(int id) {
        String message = "User with id " + id + "is not existed";
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById((int)id);
    }

    @Override
    public void updateUser(int id, User user) {
        userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setId(id);
        userRepository.save(user);
    }

    @Transactional
    public boolean registerDefaultUser(User user) {
        Optional<User> userDB  = userRepository.findUserByUsername(user.getUsername());

        if (!userDB.isEmpty()) {
            return false;
        }

        Set<Role> roles = new HashSet<>();
        Role roleUser = roleRepository.findRoleByRoleName("ROLE_USER");
        user.setUserRoles(Collections.singleton(roleUser));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    public List <Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role findRoleById(int id) {
        return roleRepository.findRoleById(id);
    }
}
