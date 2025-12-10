package net.javaguides.pslspotlightspring.services;

import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUserProfile(Long userId, String username, String teamSupported, String profilePicture) {
        User user = getUserProfile(userId);
        if (username != null) user.setUsername(username);
        if (teamSupported != null) user.setTeamSupported(teamSupported);
        if (profilePicture != null) user.setProfilePicture(profilePicture);
        return userRepository.save(user);
    }

    public void followUser(Long currentUserId, Long targetUserId) {
        if (currentUserId.equals(targetUserId)) {
            throw new RuntimeException("You cannot follow yourself");
        }

        User currentUser = getUserProfile(currentUserId);
        User targetUser = getUserProfile(targetUserId);

        currentUser.getFollowing().add(targetUser);
        targetUser.getFollowers().add(currentUser);

        userRepository.save(currentUser);
        userRepository.save(targetUser);
    }

    public void unfollowUser(Long currentUserId, Long targetUserId) {
        User currentUser = getUserProfile(currentUserId);
        User targetUser = getUserProfile(targetUserId);

        currentUser.getFollowing().remove(targetUser);
        targetUser.getFollowers().remove(currentUser);

        userRepository.save(currentUser);
        userRepository.save(targetUser);
    }
}