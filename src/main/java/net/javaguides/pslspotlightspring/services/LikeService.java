package net.javaguides.pslspotlightspring.services;
import net.javaguides.pslspotlightspring.entities.Like;
import net.javaguides.pslspotlightspring.entities.Post;
import net.javaguides.pslspotlightspring.entities.User;
import net.javaguides.pslspotlightspring.repositories.LikeRepository;
import net.javaguides.pslspotlightspring.repositories.PostRepository;
import net.javaguides.pslspotlightspring.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TrendingService trendingService;

    public LikeService(LikeRepository likeRepository,
                       PostRepository postRepository,
                       UserRepository userRepository,
                       TrendingService trendingService) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.trendingService = trendingService;
    }

    public Like likePost(Long postId, Long userId) {
        if (likeRepository.findByPostIdAndUserId(postId, userId).isPresent()) {
            throw new RuntimeException("Already liked this post");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Like like = Like.builder().post(post).user(user).build();
        Like saved = likeRepository.save(like);
        trendingService.addLikeImpact(post.getPlayer().getId());
        return saved;
    }

    public void unlikePost(Long postId, Long userId) {
        Like like = likeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new RuntimeException("Like not found"));

        trendingService.removeLikeImpact(like.getPost().getPlayer().getId());
        likeRepository.delete(like);
    }

    public List<Like> getLikesByPost(Long postId) {
        return likeRepository.findByPostId(postId);
    }
}