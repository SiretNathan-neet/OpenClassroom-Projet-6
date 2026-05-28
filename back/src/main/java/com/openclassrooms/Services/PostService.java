package com.openclassrooms.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.DTO.Request.CreateCommentRequestDTO;
import com.openclassrooms.DTO.Request.CreatePostRequestDTO;
import com.openclassrooms.DTO.Response.CommentResponseDTO;
import com.openclassrooms.DTO.Response.PostDetailResponseDTO;
import com.openclassrooms.DTO.Response.PostResponseDTO;
import com.openclassrooms.Exceptions.NotFoundException;
import com.openclassrooms.Models.CommentEntity;
import com.openclassrooms.Models.PostEntity;
import com.openclassrooms.Models.SubscriptionEntity;
import com.openclassrooms.Models.TopicEntity;
import com.openclassrooms.Models.UserEntity;
import com.openclassrooms.Repositories.CommentRepository;
import com.openclassrooms.Repositories.PostRepository;
import com.openclassrooms.Repositories.SubscriptionRepository;
import com.openclassrooms.Repositories.TopicRepository;
import com.openclassrooms.Repositories.UserRepository;

/**
 * Service gérant la logique métier des articles et commentaires.
 */
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Récupère l'utilisateur connecté depuis le SecurityContext.
     * Le nom stocké dans le token JWT correspond à l'email de l'utilisateur.
     */
    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));
    }

    /**
     * Convertit un PostEntity en PostResponseDTO.
     * Évite d'exposer l'entité directement dans les réponses API.
     */
    private PostResponseDTO toDTO(PostEntity post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorUsername(post.getAuthor().getUsername());
        dto.setTopicName(post.getTopic().getName());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }

    /**
     * Retourne le fil d'actualité de l'utilisateur connecté.
     * Filtre uniquement les articles des thèmes auxquels il est abonné.
     *
     * @param sort "desc" pour du plus récent au plus ancien (défaut), "asc" pour l'inverse
     */
    public List<PostResponseDTO> getFeed(String sort) {
        UserEntity user = getCurrentUser();

        List<TopicEntity> subscribedTopics = subscriptionRepository
            .findByUser(user)
            .stream()
            .map(SubscriptionEntity::getTopic)
            .collect(Collectors.toList());

        if (subscribedTopics.isEmpty()) {
            return List.of();
        }

        List<PostEntity> posts;
        if ("asc".equalsIgnoreCase(sort)) {
            posts = postRepository.findByTopicInOrderByCreatedAtAsc(subscribedTopics);
        } else {
            posts = postRepository.findByTopicInOrderByCreatedAtDesc(subscribedTopics);
        }

        return posts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PostResponseDTO createPost(CreatePostRequestDTO request) {
        
        UserEntity author = getCurrentUser();

        TopicEntity topic = topicRepository.findById(request.getTopicId())
            .orElseThrow(() -> new NotFoundException("Thème introuvable"));
        
        PostEntity post = new PostEntity();
        post.setAuthor(author);
        post.setTopic(topic);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        PostEntity saved = postRepository.save(post);
        return toDTO(saved);
    }

    public PostDetailResponseDTO getPostById(Integer id) {
        PostEntity post = postRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Article introuvable"));

        List<CommentResponseDTO> comments = commentRepository
            .findByPostOrderByCreatedAtAsc(post)
            .stream()
            .map(comment -> {
                CommentResponseDTO dto = new CommentResponseDTO();
                dto.setId(comment.getId());
                dto.setAuthorUsername(comment.getAuthor().getUsername());
                dto.setContent(comment.getContent());
                dto.setCreatedAt(comment.getCreatedAt());
                return dto;
            })
            .collect(Collectors.toList());

        PostDetailResponseDTO dto = new PostDetailResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorUsername(post.getAuthor().getUsername());
        dto.setTopicName(post.getTopic().getName());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setComments(comments);

        return dto;
    }

    public CommentResponseDTO addComment(Integer postId, CreateCommentRequestDTO request) {
        UserEntity author = getCurrentUser();

        PostEntity post = postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundException("Article introuvable"));

        CommentEntity comment = new CommentEntity();
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setContent(request.getContent());

        CommentEntity saved = commentRepository.save(comment);

        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(saved.getId());
        dto.setAuthorUsername(saved.getAuthor().getUsername());
        dto.setContent(saved.getContent());
        dto.setCreatedAt(saved.getCreatedAt());

        return dto;
    }
}
