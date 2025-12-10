package net.javaguides.pslspotlightspring.services;

import net.javaguides.pslspotlightspring.entities.Comment;
import net.javaguides.pslspotlightspring.entities.Post;
import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.repositories.CommentRepository;
import net.javaguides.pslspotlightspring.repositories.PostRepository;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import net.javaguides.pslspotlightspring.dto.CommentDto;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TrendingService trendingService;
    private final NotificationService notificationService; // NEW

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository,
                          TrendingService trendingService,
                          NotificationService notificationService) { // NEW
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.trendingService = trendingService;
        this.notificationService = notificationService; // NEW
    }

    public CommentDto createComment(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();

        Comment saved = commentRepository.save(comment);

        // 🔼 Trending impact
        trendingService.addCommentImpact(post.getPlayer().getId());

        // 🔔 Notification
        notificationService.sendNotification(
                post.getUser().getId(),
                "COMMENT",
                user.getUsername() + " commented on your post"
        );

        return CommentDto.from(saved);
    }

    public List<CommentDto> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(CommentDto::from)
                .toList();
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized");
        }

        trendingService.removeCommentImpact(comment.getPost().getPlayer().getId());
        commentRepository.delete(comment);
    }
}