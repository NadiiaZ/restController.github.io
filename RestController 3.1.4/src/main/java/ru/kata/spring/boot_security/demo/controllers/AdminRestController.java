package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.servises.UserServiceImp;
import ru.kata.spring.boot_security.demo.utils.UserErrorResponse;
import ru.kata.spring.boot_security.demo.utils.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.utils.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {
    private final UserServiceImp userService;

    @Autowired
    public AdminRestController(UserServiceImp userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.showAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable int id) {
        return convertToUserDTO(userService.showUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser (@PathVariable("id") int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser (
            @PathVariable("id") int id,
            @RequestBody String rolesId,
            @RequestBody @Valid UserDTO userDTOUpdate,
            BindingResult bindingResult) {
        bindingResultErrors(bindingResult);

        userService.updateUser(id, convertToUser(userDTOUpdate));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> addUser(@RequestBody @Valid UserDTO userDTO,
                                              BindingResult bindingResult) {
        bindingResultErrors(bindingResult);

        userService.save(convertToUser(userDTO));
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

    private User convertToUser(UserDTO userDTO) {
        return new ModelMapper().map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO(User user) {
        return new ModelMapper().map(user, UserDTO.class);
    }

    private void bindingResultErrors (BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                errors.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage());
            }
            throw new UserNotCreatedException(errors.toString());
        }
    }
}
