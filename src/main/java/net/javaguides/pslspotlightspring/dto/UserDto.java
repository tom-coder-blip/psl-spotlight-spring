package net.javaguides.pslspotlightspring.dto;

import lombok.Getter;
import lombok.Setter;
import net.javaguides.pslspotlightspring.entities.User;
import java.util.List;

@Getter @Setter
public class UserDto {
    private Long id;
    private String username;
    private String profilePicture;
    private String teamSupported;
    private List<String> followers;
    private List<String> following;

    public static UserDto from(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setTeamSupported(user.getTeamSupported());
        dto.setFollowers(user.getFollowers().stream().map(User::getUsername).toList());
        dto.setFollowing(user.getFollowing().stream().map(User::getUsername).toList());
        return dto;
    }
}
