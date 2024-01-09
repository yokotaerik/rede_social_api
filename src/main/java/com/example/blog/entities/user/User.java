package com.example.blog.entities.user;

import com.example.blog.entities.comments.Comment;
import com.example.blog.entities.post.Post;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private String about = "";

    @DBRef(lazy = true)
    private List<User> followers = new ArrayList<>();

    @DBRef(lazy = true)
    private List<User> following = new ArrayList<>();

    @DBRef(lazy = true)
    private List<Post> likedPosts = new ArrayList<>();

    @DBRef(lazy = true)
    private List<Comment> likedComments = new ArrayList<>();

    @DBRef(lazy = true)
    private List<Post> posts = new ArrayList<>();

    @DBRef(lazy = true)
    private List<Comment> comments = new ArrayList<>();
    private LocalDate createdAt;

    public User(String id, String username, String email, String password, LocalDate createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = UserRole.USER;
        this.createdAt = createdAt;
    }

    public void addFollower(User follower) {
        followers.add(follower);
    }

    public void removeFollower(User follower) {
        followers.remove(follower);
    }

    public void addFollowing(User followed) {
        following.add(followed);
    }

    public void removeFollowing(User followed) {
        following.remove(followed);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implemente a lógica conforme necessário
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implemente a lógica conforme necessário
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implemente a lógica conforme necessário
    }

    @Override
    public boolean isEnabled() {
        return true; // Implemente a lógica conforme necessário
    }
}