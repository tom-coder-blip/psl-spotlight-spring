package net.javaguides.pslspotlightspring.controllers;

import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        User user = authService.register(
                body.get("username"),
                body.get("email"),
                body.get("password"),
                body.get("teamSupported")
        );
        return ResponseEntity.ok(Map.of("message", "User registered successfully", "id", user.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String token = authService.login(body.get("email"), body.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(@RequestAttribute("userId") Long userId) {
        authService.deleteAccount(userId);
        return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String token = authService.forgotPassword(body.get("email"));
        return ResponseEntity.ok(Map.of("resetToken", token));
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable String token,
                                           @RequestBody Map<String, String> body) {
        authService.resetPassword(token, body.get("newPassword"));
        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }

    @PostMapping("/test-user")
    public ResponseEntity<?> testUserBuilder() {
        User user = User.builder()
                .username("tomvuma")
                .email("tom@example.com")
                .password("securepass")
                .build();

        Long id = user.getId(); // Should compile now
        return ResponseEntity.ok("User ID: " + id);
    }
}
