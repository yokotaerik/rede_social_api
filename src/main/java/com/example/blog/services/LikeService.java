package com.example.blog.services;

import com.example.blog.entities.like.Like;
import com.example.blog.entities.like.dtos.LikeDTO;
import com.example.blog.entities.post.Post;
import com.example.blog.repositories.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    PostService postService;

    public void likeOrDislike(LikeDTO data) {
        Optional<Post> optionalPost = postService.findById(data.postId());

        if (optionalPost.isPresent()) {
            Like existingLike = likeRepository.findByPostIdAndUserId(data.postId(), data.userId());

            if (existingLike != null) {
                likeRepository.delete(existingLike);
                optionalPost.get().setLike(optionalPost.get().getLike() - 1);
            } else {
                Like like = new Like(null, data.postId(), data.userId());
                likeRepository.save(like);
                optionalPost.get().setLike(optionalPost.get().getLike() + 1);
            }

            postService.save(optionalPost.get());
        }
    }
}
