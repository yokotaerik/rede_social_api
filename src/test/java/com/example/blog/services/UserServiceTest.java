package com.example.blog.services;

import com.example.blog.entities.post.Post;
import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.CreateUserDTO;
import com.example.blog.repositories.UserRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {


    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShouldCreateUserWhenUsernameAndEmailAreUnique() throws BadRequestException {
        CreateUserDTO userDTO = new CreateUserDTO("", "uniqueUsername", "testPassword", "uniqueEmail");

        when(userRepository.findByEmail(userDTO.email())).thenReturn(null);
        when(userRepository.findByUsername(userDTO.username())).thenReturn(null);

        User user = userService.create(userDTO);

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(userDTO.username(), user.getUsername());
        assertEquals(userDTO.email(), user.getEmail());
        assertNotEquals(userDTO.password(), user.getPassword());
    }

    @Test
    void createShouldThrowExceptionWhenEmailAlreadyExists() {
        CreateUserDTO userDTO = new CreateUserDTO("", "testUser", "testPassword", "existingEmail");

        when(userRepository.findByEmail(userDTO.email())).thenReturn(new User());

        assertThrows(BadRequestException.class, () -> userService.create(userDTO));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void createShouldThrowExceptionWhenUsernameAlreadyExists() {
        CreateUserDTO userDTO = new CreateUserDTO("", "existingUsername", "testPassword", "testEmail");

        when(userRepository.findByUsername(userDTO.username())).thenReturn(new User());

        assertThrows(BadRequestException.class, () -> userService.create(userDTO));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void findAllShouldReturnAllUsers() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(users.size(), result.size());
    }

    @Test
    void followShouldAddFollowerWhenNotAlreadyFollowing() {
        User follower = new User();
        User followed = new User();

        userService.follow(follower, followed);

        assertTrue(followed.getFollowers().contains(follower));
        assertTrue(follower.getFollowing().contains(followed));
    }

    @Test
    void followShouldRemoveFollowerWhenAlreadyFollowing() {
        User follower = new User();
        User followed = new User();
        followed.addFollower(follower);
        follower.addFollowing(followed);

        userService.follow(follower, followed);

        assertFalse(followed.getFollowers().contains(follower));
        assertFalse(follower.getFollowing().contains(followed));
    }

    @Test
    void getFeedShouldReturnPostsFromFollowedUsers() {
        User user = new User();
        User followed = new User();
        Post post = new Post();
        followed.getPosts().add(post);
        user.addFollowing(followed);

        List<Post> result = userService.getFeed(user);

        assertTrue(result.contains(post));
    }

    @Test
    void findUsersByUsernameShouldReturnUsersWithMatchingUsername() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByRegexInUsername("testUser")).thenReturn(List.of(user));

        List<User> result = userService.findUsersByUsername("testUser");

        assertTrue(result.contains(user));
    }
}