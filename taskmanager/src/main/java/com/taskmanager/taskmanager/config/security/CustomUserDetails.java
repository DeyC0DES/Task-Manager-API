package com.taskmanager.taskmanager.config.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmanager.models.UserModel;
import com.taskmanager.taskmanager.services.UserService;

@Service
public class CustomUserDetails implements UserDetailsService {
    
    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel model = service.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        return new User(model.getEmail(), model.getPassword(), new ArrayList<>());
    }



}
