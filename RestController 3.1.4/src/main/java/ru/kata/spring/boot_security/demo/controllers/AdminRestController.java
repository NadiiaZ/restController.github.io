package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImp;
import ru.kata.spring.boot_security.demo.servises.UserServiceImp;
import ru.kata.spring.boot_security.demo.utils.UserErrorResponse;
import ru.kata.spring.boot_security.demo.utils.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.utils.UserNotFoundException;
import ru.kata.spring.boot_security.demo.utils.Util;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/rest/admin")
public class AdminRestController {
    private final UserServiceImp userService;
    private final PasswordEncoder passwordEncoder;
    private final Util util;

    @Autowired
    public AdminRestController(UserServiceImp userService,
                               PasswordEncoder passwordEncoder,
                               Util util) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.util = util;
    }


    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.showAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable int id) {
        return util.convertToUserDTO(userService.showUserById(id));
    }

    @GetMapping("/authUser")
    public UserDTO authUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImp userDetails = (UserDetailsImp) auth.getPrincipal();
        return util.convertToUserDTO(userDetails.getUser());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser (@PathVariable("id") int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", consumes = {"application/xml","application/json"})
    public ResponseEntity<HttpStatus> updateUser (
            @PathVariable("id") int id,
            @RequestBody @Valid UserDTO userDTOUpdate,
            BindingResult bindingResult) {
        util.bindingResultErrors(bindingResult);

        userDTOUpdate.setPassword(passwordEncoder.encode(userDTOUpdate.getPassword()));
        userService.updateUser(id, util.convertToUser(userDTOUpdate));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping(value = "/new", consumes = {"application/xml","application/json"})
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid UserDTO userDTO,
                                              BindingResult bindingResult) {
        util.bindingResultErrors(bindingResult);
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userService.save(util.convertToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException (UserNotFoundException e) {
        String msg = "User with this ID wasn't found!";
        UserErrorResponse response = new UserErrorResponse(msg, System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException (UserNotCreatedException e) {
        String msg = e.getMessage();
        UserErrorResponse response = new UserErrorResponse(msg, System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
