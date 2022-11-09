package ru.kata.spring.boot_security.demo.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name= "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "firstname")
    @NotEmpty
    @Size(min = 2, max = 30)
    private String name;
    @Column(name = "lastname")
    @NotEmpty
    @Size(min = 2, max = 30)
    private String surname;
    @Column(name = "username")
    @NotEmpty
    @Size(min = 2, max = 30)
    private String username;
    @Column(name = "email")
    @NotEmpty
    @Email
    private String email;
    @Column(name="password")
    @NotEmpty
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )

    private Set<Role> userRoles;

    public void addNewRole(Role role) {
        if (userRoles == null) {
            userRoles = new HashSet<>();
        }
        userRoles.add(role);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User() {

    }
    public Set<Role> getUserRoles() {
        return userRoles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public void setUserRoles(Set<Role> roles) {
        this.userRoles = roles;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStringRoles() {
        return Arrays.toString(userRoles.toArray());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}