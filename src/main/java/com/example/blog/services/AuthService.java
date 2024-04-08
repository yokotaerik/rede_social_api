package com.example.blog.services;

import com.example.blog.entities.user.User;
import com.example.blog.entities.user.dtos.LoginDTO;
import com.example.blog.infra.security.TokenService;
import com.example.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user =  repository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public String authenticateUser(LoginDTO credentials) {
        try {
            // Verifica as credenciais, se forem validas ele envia o token, caso contrário ele retorna nulo
            var usernamePassword = new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());
            var auth = authenticationManager.authenticate(usernamePassword);
            return tokenService.generateToken((User) auth.getPrincipal());
        } catch (AuthenticationException e) {
            return null;
        }
    }

    // A partir da requisição ele pega o usuário via token, essa lógica deriva do spring security e assim carrega o usuário, a função também trata
    // eventuais erros
    public User getCurrentUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails == null) {
            throw new Exception("Internal server error on get user");
        }

        return (User) loadUserByUsername(userDetails.getUsername());
    }


}