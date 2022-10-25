package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImp;
import ru.kata.spring.boot_security.demo.servises.UserServiceImp;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImp userService;

    @Autowired
    public AdminController(UserServiceImp userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String index(Model model, @ModelAttribute("userForm") User userForm) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImp userDetails = (UserDetailsImp) auth.getPrincipal();
        model.addAttribute("authUser", userDetails.getUser());

        List<Role> roles = userService.getAllRoles();
        model.addAttribute("roles",roles);

        List<User> users = userService.showAllUsers();
        model.addAttribute("user",users);

        return "admin/index";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
    @PostMapping("/new")
    public String addUser(@ModelAttribute ("userForm") @Valid User formUser,
                          BindingResult bindingResult, Model model) {
//        if(bindingResult.hasErrors()) {
//            return "admin/index";
//        }
        if(!formUser.getPassword().equals(formUser.getPasswordConfirm())) {
            model.addAttribute("passwordError", "Different passwords!");
            return "admin/index";
        }
        userService.save(formUser);
//        if(!userService.registerDefaultUser(formUser)) {
//            model.addAttribute("usernameError",
//                    "User with this email or password already exists");
//            return "admin/index";
//        }

        return "redirect:/admin";
    }

    @PatchMapping ("/{id}")
    public String update(@PathVariable("id") int id,
                         @RequestParam(value = "rolesId", required = false) String rolesId,
                         @ModelAttribute("userForm") User userUpdate) {

        userService.updateUser(id, userUpdate);
        return "redirect:/admin";
    }
}