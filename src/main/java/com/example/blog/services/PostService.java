package com.example.blog.services;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.post.Post;
import com.example.blog.entities.post.dtos.CreatePostDTO;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.UserRole;
import com.example.blog.exceptions.EntityNotFoundException;
import com.example.blog.repositories.CommentRepository;
import com.example.blog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserService userService;

    @Autowired
    CommentRepository commentRepository;

    public void create(CreatePostDTO data, User user){

        LocalDate now = LocalDate.now();
        Post post = new Post(data.title(), data.content(), user, now);

        postRepository.save(post);
        user.getPosts().add(post);
        userService.save(user);
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    public Post findById(String id) throws EntityNotFoundException {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }


    public void save(Post post) {  postRepository.save(post); }

    public void likeOrDislike(Post post, User user){

        if(post.getLikes().contains(user)){
            post.getLikes().remove(user);
            user.getLikedPosts().remove(post);
        } else {
            post.getLikes().add(user);
            user.getLikedPosts().add(post);
        }

        save(post);
        userService.save(user);

    }

    public void delete(Post post, User whoRequestDelete) {

        if (post.getAuthor().equals(whoRequestDelete) || whoRequestDelete.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(UserRole.ADMIN.name()))) {


            post.getAuthor().getPosts().remove(post);
            userService.save(post.getAuthor());

            List<Comment> comments = post.getComments();
            for (Comment comment : comments) {
                List<User> likedUsers = comment.getLikes();
                for (User user : likedUsers) {
                    user.getLikedComments().remove(comment);
                    userService.save(user);
                }
                commentRepository.delete(comment);
            }

            List<User> likedUsers = post.getLikes();
            for (User user : likedUsers) {
                user.getLikedPosts().remove(post);
                userService.save(user);
            }


            postRepository.delete(post);
        } else {
            throw new SecurityException("You do not have permission to delete this post");
        }
    }
}
