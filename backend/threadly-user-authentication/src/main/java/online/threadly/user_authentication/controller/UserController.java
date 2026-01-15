package online.threadly.user_authentication.controller;

import online.threadly.user_authentication.dao.SignUpRequest;
import online.threadly.user_authentication.model.User;
import online.threadly.user_authentication.service.AuthService;
import online.threadly.user_authentication.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableMethodSecurity(prePostEnabled = true)
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;
  private final AuthService authService;

  public UserController(UserService userService, AuthService authService) {
    this.userService = userService;
    this.authService = authService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    this.authService.registerUser(
        new SignUpRequest(user.getName(), user.getEmail(), user.getPassword()));
    User createdUser = this.userService.findByEmail(user.getEmail()).orElseThrow();
    return ResponseEntity.ok(createdUser);
  }

  @GetMapping("/me")
  public ResponseEntity<User> findProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok(user);
  }

  @GetMapping("/{email}")
  public ResponseEntity<User> findByEmail(@PathVariable String email) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    if (!user.getEmail().equals(email)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    return ResponseEntity.ok(user);
  }
}
