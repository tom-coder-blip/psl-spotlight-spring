package net.javaguides.pslspotlightspring.services;

import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import net.javaguides.pslspotlightspring.security.JwtTokenProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import net.javaguides.pslspotlightspring.services.EmailService;
import net.javaguides.pslspotlightspring.entities.Role;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    public User register(String username, String email, String password, String teamSupported) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .teamSupported(teamSupported)
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        // Send welcome email
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getUsername());

        return savedUser;
    }

    public String login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) throw new RuntimeException("Invalid credentials");

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtTokenProvider.generateToken(user.getId(), user.getRole().name());
    }

    public void deleteAccount(Long userId) {
        userRepository.deleteById(userId);
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordExpires(Instant.now().plusSeconds(3600));
        userRepository.save(user);

        // Send password reset email
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);

        return resetToken;
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getResetPasswordToken())
                        && u.getResetPasswordExpires().isAfter(Instant.now()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpires(null);
        userRepository.save(user);
    }
}
