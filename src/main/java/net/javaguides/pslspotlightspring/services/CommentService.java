package net.javaguides.pslspotlightspring.services;

import net.javaguides.pslspotlightspring.entities.Comment;
import net.javaguides.pslspotlightspring.entities.Post;
import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.repositories.CommentRepository;
import net.javaguides.pslspotlightspring.repositories.PostRepository;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TrendingService trendingService;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository,
                          TrendingService trendingService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.trendingService = trendingService;
    }

    public Comment createComment(Long postId, Long userId, String content) {
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
        trendingService.addCommentImpact(post.getPlayer().getId());
        return saved;
    }

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
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
