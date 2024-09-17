package com.taskmanager.taskmanager.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDto {
    
    private String email;
    private String username;
    private String password;

}
