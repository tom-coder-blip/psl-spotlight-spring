package net.javaguides.pslspotlightspring.controllers;

import net.javaguides.pslspotlightspring.entities.Comment;
import net.javaguides.pslspotlightspring.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import net.javaguides.pslspotlightspring.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import net.javaguides.pslspotlightspring.dto.CommentDto;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId,
                                                    @RequestBody Map<String, String> body,
                                                    @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal != null ? principal.getUserId() : null;
        Comment comment = commentService.createComment(postId, userId, body.get("content"));

        CommentDto dto = new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUsername(),
                comment.getCreatedAt()
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPost(postId);

        List<CommentDto> dtos = comments.stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getContent(),
                        c.getUser().getUsername(),
                        c.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id,
                                           @AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal != null ? principal.getUserId() : null;
        commentService.deleteComment(id, userId);
        return ResponseEntity.ok("Comment deleted");
    }
}