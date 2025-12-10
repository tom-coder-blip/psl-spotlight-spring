package net.javaguides.pslspotlightspring.controllers;

import net.javaguides.pslspotlightspring.entities.Like;
import net.javaguides.pslspotlightspring.services.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import net.javaguides.pslspotlightspring.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import net.javaguides.pslspotlightspring.dto.LikeDto;
import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<LikeDto> likePost(@PathVariable Long postId,
                                            @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal != null ? principal.getUserId() : null;
        Like like = likeService.likePost(postId, userId);

        LikeDto dto = new LikeDto(
                like.getId(),
                like.getUser().getUsername(),
                like.getCreatedAt()
        );

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId,
                                        @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal != null ? principal.getUserId() : null;
        likeService.unlikePost(postId, userId);
        return ResponseEntity.ok("Post unliked");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<LikeDto>> getLikes(@PathVariable Long postId) {
        List<Like> likes = likeService.getLikesByPost(postId);

        List<LikeDto> dtos = likes.stream()
                .map(like -> new LikeDto(
                        like.getId(),
                        like.getUser().getUsername(),
                        like.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
