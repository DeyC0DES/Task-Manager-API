package com.taskmanager.taskmanager.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegisterRecordDto(@NotNull @NotEmpty @Email String email,
                                @NotNull @NotEmpty String username,
                                @NotNull @NotEmpty String password) {
    
}
