package com.cse.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.cse.api.exception.BadRequest;
import com.cse.api.exception.ResourceNotFoundException;
import com.cse.api.model.User;
import com.cse.api.model.Login;
import com.cse.api.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @PostMapping("/users")
  public User createUser(@Valid @RequestBody User user) throws ResourceNotFoundException {
    User savedUser = userRepository.findByEmailAddress(user.getEmail());
    final String email = user.getEmail();
    if (savedUser != null && email.equals(savedUser.getEmail())) {
      throw new BadRequest("User with email:: " + email + " already exists");
    }
    return userRepository.save(user);
  }

  @PostMapping("/login")
  public User login(@Valid @RequestBody Login user) throws ResourceNotFoundException {
    User foundUser = userRepository.login(user.email, user.password);
    if (foundUser == null) {
      throw new BadRequest("Invalid login");
    }
    return foundUser;
  }

  @GetMapping("/users")
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId)
      throws ResourceNotFoundException {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found :: " + userId));
    return ResponseEntity.ok().body(user);
  }

  @DeleteMapping("/users/{id}")
  public Map<String, Boolean> deletUser(@PathVariable(value = "id") Long userId)
      throws ResourceNotFoundException {
    User contact = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found :: " + userId));
    userRepository.delete(contact);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
      @Valid @RequestBody User userDetails) throws ResourceNotFoundException {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found :: " + userId));
    user.setEmail(userDetails.getEmail());
    final User updatedUser = userRepository.save(user);
    return ResponseEntity.ok(updatedUser);
  }

}
