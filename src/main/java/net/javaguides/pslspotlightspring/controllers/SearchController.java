package net.javaguides.pslspotlightspring.controllers;

import net.javaguides.pslspotlightspring.entities.Player;
import net.javaguides.pslspotlightspring.entities.Post;
import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.repositories.PlayerRepository;
import net.javaguides.pslspotlightspring.repositories.PostRepository;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import net.javaguides.pslspotlightspring.dto.UserDto;
import net.javaguides.pslspotlightspring.dto.PlayerDto;
import net.javaguides.pslspotlightspring.dto.PostDto;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final PlayerRepository playerRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public SearchController(PlayerRepository playerRepository, PostRepository postRepository, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/players")
    public ResponseEntity<List<PlayerDto>> searchPlayers(@RequestParam String keyword) {
        List<Player> results = playerRepository.findByNameContainingIgnoreCaseOrClubContainingIgnoreCase(keyword, keyword);
        List<PlayerDto> dtos = results.stream()
                .map(p -> new PlayerDto(
                        p.getId(),
                        p.getName(),
                        p.getClub(),
                        p.getPosition(),
                        p.getPlayerPicture(),
                        p.getGoals(),
                        p.getAssists(),
                        p.getAppearances(),
                        p.getTrendingRating(),
                        p.getCreatedAt()
                ))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> searchPosts(@RequestParam String keyword) {
        List<Post> results = postRepository.findByTitleContainingIgnoreCaseOrTagsContainingIgnoreCase(keyword, keyword);
        List<PostDto> dtos = results.stream().map(post -> new PostDto(
                post.getId(),
                post.getPlayerName(),
                post.getClub(),
                post.getMatchweek(),
                post.getTitle(),
                post.getContent(),
                post.getImage(),
                post.getUser().getUsername(),
                post.getTags(),
                post.getLikes().size(),
                post.getCreatedAt()
        )).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String keyword) {
        List<User> results = userRepository.findByUsernameContainingIgnoreCaseOrTeamSupportedContainingIgnoreCase(keyword, keyword);
        List<UserDto> dtos = results.stream().map(UserDto::from).toList();
        return ResponseEntity.ok(dtos);
    }
}