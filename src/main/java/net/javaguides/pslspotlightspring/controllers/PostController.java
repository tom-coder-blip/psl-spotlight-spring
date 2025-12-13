package net.javaguides.pslspotlightspring.controllers;

import net.javaguides.pslspotlightspring.entities.Post;
import net.javaguides.pslspotlightspring.services.PostService;
import net.javaguides.pslspotlightspring.repositories.PlayerRepository;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import net.javaguides.pslspotlightspring.security.UserPrincipal;
import net.javaguides.pslspotlightspring.dto.PostDto;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;

    public PostController(PostService postService, UserRepository userRepository, PlayerRepository playerRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestParam("playerId") Long playerId,
            @RequestParam("playerName") String playerName,
            @RequestParam("club") String club,
            @RequestParam("matchweek") int matchweek,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        try {
            // Extract userId from principal
            Long userId = principal != null ? principal.getUserId() : null;

            // Debug prints
            System.out.println("DEBUG: userId = " + userId);
            System.out.println("DEBUG: playerId = " + playerId);

            if (userId == null || playerId == null) {
                System.out.println("ERROR: userId or playerId is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            String filename = null;
            if (image != null && !image.isEmpty()) {
                filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path uploadDir = Paths.get("uploads");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                Path path = uploadDir.resolve(filename);
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            Post post = Post.builder()
                    .user(userRepository.findById(userId).orElseThrow())
                    .player(playerRepository.findById(playerId).orElseThrow())
                    .playerName(playerName)
                    .club(club)
                    .matchweek(matchweek)
                    .title(title)
                    .content(content)
                    .image(filename)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(post));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getPosts(@RequestParam(required = false) Integer week) {
        List<Post> posts = postService.getPosts(week);
        List<PostDto> dtos = posts.stream().map(post -> new PostDto(
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

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);

        PostDto dto = new PostDto(
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
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> editPost(@PathVariable Long id, @RequestBody Post updated,
                                         @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal != null ? principal.getUserId() : null;
        return ResponseEntity.ok(postService.editPost(id, updated, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal != null ? principal.getUserId() : null;
        postService.deletePost(id, userId);
        return ResponseEntity.ok().body("Post deleted successfully");
    }
}