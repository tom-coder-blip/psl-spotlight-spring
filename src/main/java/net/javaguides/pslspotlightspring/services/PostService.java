package net.javaguides.pslspotlightspring.services;

import net.javaguides.pslspotlightspring.entities.Post;
import net.javaguides.pslspotlightspring.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final TrendingService trendingService;

    public PostService(PostRepository postRepository, TrendingService trendingService) {
        this.postRepository = postRepository;
        this.trendingService = trendingService;
    }

    @Transactional
    public Post createPost(Post post) {
        Post saved = postRepository.save(post);

        // Call TrendingService to increase rating
        trendingService.addPostImpact(saved.getPlayer().getId());

        return saved;
    }

    public List<Post> getPosts(Integer week) {
        if (week != null) {
            return postRepository.findByMatchweek(week);
        }
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Transactional
    public Post editPost(Long id, Post updated, Long userId) {
        Post post = getPostById(id);
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized");
        }
        post.setTitle(updated.getTitle());
        post.setContent(updated.getContent());
        post.setTags(updated.getTags());
        post.setImage(updated.getImage());
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id, Long userId) {
        Post post = getPostById(id);
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not authorized");
        }

        // Call TrendingService to decrease rating
        trendingService.removePostImpact(post.getPlayer().getId());

        postRepository.delete(post);
    }
}