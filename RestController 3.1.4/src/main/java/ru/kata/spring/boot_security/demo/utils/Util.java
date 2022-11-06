package ru.kata.spring.boot_security.demo.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.kata.spring.boot_security.demo.DTO.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

@Component
public class Util {
    public void bindingResultErrors (BindingResult bindingResult) {
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

    public User convertToUser(UserDTO userDTO) {
        return new ModelMapper().map(userDTO, User.class);
    }

    public UserDTO convertToUserDTO(User user) {
        return new ModelMapper().map(user, UserDTO.class);
    }

}
