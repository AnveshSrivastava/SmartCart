package online.threadly.user_authentication.controller;

import online.threadly.user_authentication.dao.AuthResponse;
import online.threadly.user_authentication.dao.SignInRequest;
import online.threadly.user_authentication.dao.SignUpRequest;
import online.threadly.user_authentication.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
    return ResponseEntity.ok(authService.registerUser(signUpRequest));
  }

  @PostMapping("/login")
  @CrossOrigin
  public ResponseEntity<AuthResponse> loginUser(@RequestBody SignInRequest signInRequest) {
    return ResponseEntity.ok(authService.authenticate(signInRequest));
  }
}
