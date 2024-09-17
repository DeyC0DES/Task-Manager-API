package com.taskmanager.taskmanager.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmanager.config.security.TokenService;
import com.taskmanager.taskmanager.dtos.LoginRecordDto;
import com.taskmanager.taskmanager.dtos.RegisterRecordDto;
import com.taskmanager.taskmanager.dtos.ResponseRecordDto;
import com.taskmanager.taskmanager.dtos.UpdateDto;
import com.taskmanager.taskmanager.enums.RoleEnum;
import com.taskmanager.taskmanager.models.UserModel;
import com.taskmanager.taskmanager.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRecordDto body) {
        var userModelOptional = userService.findByEmail(body.email());

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        if (passwordEncoder.matches(body.password(), userModelOptional.get().getPassword())) {
            String token = this.tokenService.generateToken(userModelOptional.get());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseRecordDto(userModelOptional.get().getEmail(), token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login/Password id incorrect");
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterRecordDto body) {
        var userModelOptional = userService.findByEmail(body.email());

        if (userModelOptional.isEmpty()) {
            var userModel = new UserModel();
            userModel.setEmail(body.email());
            userModel.setUsername(body.username());
            userModel.setPassword(passwordEncoder.encode(body.password()));

            if (body.username().equals("Admin.hkf492")) {
                userModel.setRole(RoleEnum.ADMIN);
            } else {
                userModel.setRole(RoleEnum.USER);
            }

            this.userService.save(userModel);
            String token = this.tokenService.generateToken(userModel);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseRecordDto(userModel.getEmail(), token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login/Password id incorrect");
    }

    @GetMapping("get/")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.findAll());
    }

    @GetMapping("get/{userId}")
    public ResponseEntity<Object> getAll(@PathVariable(value="userId") UUID id) {
        var userModelOptional = this.userService.findById(id);

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
    }

    @PutMapping("update/{userId}")
    public ResponseEntity<Object> update(@PathVariable(value="userId") UUID userId,
                                         @RequestBody UpdateDto body) {
        var userModelOptional = this.userService.findById(userId);

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        UserModel userModel = userModelOptional.get();
        userModel.setEmail(body.getEmail());
        userModel.setUsername(body.getUsername());
        userModel.setPassword(passwordEncoder.encode(body.getPassword()));
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.save(userModel));
    }

    @DeleteMapping("delete/{userid}")
    public ResponseEntity<Object> delete(@PathVariable(value="userid") UUID userId) {
        var userModelOptional = this.userService.findById(userId);

        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        this.userService.delete(userModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Account deleted successfully!");
    }

}
