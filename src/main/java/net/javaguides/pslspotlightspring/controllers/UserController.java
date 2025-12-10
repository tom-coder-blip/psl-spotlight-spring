package net.javaguides.pslspotlightspring.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.services.UserService;
import net.javaguides.pslspotlightspring.dto.UserDto;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable Long id) {
        User user = userService.getUserProfile(id);
        return ResponseEntity.ok(UserDto.from(user));
    }

    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> updateProfileWithImage(@RequestAttribute("userId") Long userId,
                                                          @RequestParam(required = false) String username,
                                                          @RequestParam(required = false) String teamSupported,
                                                          @RequestParam(required = false) MultipartFile profilePicture) {
        String filename = null;

        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                filename = System.currentTimeMillis() + "_" + profilePicture.getOriginalFilename();
                Path uploadDir = Paths.get("uploads");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                Path path = uploadDir.resolve(filename);
                Files.copy(profilePicture.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        User updated = userService.updateUserProfile(userId, username, teamSupported, filename);
        return ResponseEntity.ok(UserDto.from(updated));
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<?> followUser(@PathVariable Long id,
                                        @RequestAttribute("userId") Long userId) {
        userService.followUser(userId, id);
        return ResponseEntity.ok("Followed successfully");
    }

    @PostMapping("/{id}/unfollow")
    public ResponseEntity<?> unfollowUser(@PathVariable Long id,
                                          @RequestAttribute("userId") Long userId) {
        userService.unfollowUser(userId, id);
        return ResponseEntity.ok("Unfollowed successfully");
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<UserDto>> getFollowers(@PathVariable Long id) {
        User user = userService.getUserProfile(id);
        List<UserDto> followers = user.getFollowers().stream()
                .map(UserDto::from)
                .toList();
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<List<UserDto>> getFollowing(@PathVariable Long id) {
        User user = userService.getUserProfile(id);
        List<UserDto> following = user.getFollowing().stream()
                .map(UserDto::from)
                .toList();
        return ResponseEntity.ok(following);
    }
}
