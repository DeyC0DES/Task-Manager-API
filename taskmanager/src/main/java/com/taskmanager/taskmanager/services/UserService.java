package com.taskmanager.taskmanager.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.taskmanager.models.UserModel;
import com.taskmanager.taskmanager.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserModel save(UserModel model) {
        userRepository.save(model);   
        return model;
    }

    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    public Optional<UserModel> findById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<UserModel> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void delete(UserModel model) {
        userRepository.delete(model);
    }
}
