package net.javaguides.pslspotlightspring.services;
import net.javaguides.pslspotlightspring.entities.Like;
import net.javaguides.pslspotlightspring.entities.Post;
import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.repositories.LikeRepository;
import net.javaguides.pslspotlightspring.repositories.PostRepository;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import net.javaguides.pslspotlightspring.dto.LikeDto;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TrendingService trendingService;
    private final NotificationService notificationService;

    public LikeService(LikeRepository likeRepository,
                       PostRepository postRepository,
                       UserRepository userRepository,
                       TrendingService trendingService,
                       NotificationService notificationService) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.trendingService = trendingService;
        this.notificationService = notificationService;
    }

    public LikeDto likePost(Long postId, Long userId) {
        if (likeRepository.findByPostIdAndUserId(postId, userId).isPresent()) {
            throw new RuntimeException("Already liked this post");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Like like = Like.builder().post(post).user(user).build();
        Like saved = likeRepository.save(like);

        // 🔼 Trending impact
        trendingService.addLikeImpact(post.getPlayer().getId());

        // 🔔 Notification
        notificationService.sendNotification(
                post.getUser().getId(),
                "LIKE",
                user.getUsername() + " liked your post"
        );

        return LikeDto.from(saved);
    }

    public void unlikePost(Long postId, Long userId) {
        Like like = likeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        trendingService.removeLikeImpact(like.getPost().getPlayer().getId());
        likeRepository.delete(like);
    }

    public List<LikeDto> getLikesByPost(Long postId) {
        return likeRepository.findByPostId(postId)
                .stream()
                .map(LikeDto::from)
                .toList();
    }
}