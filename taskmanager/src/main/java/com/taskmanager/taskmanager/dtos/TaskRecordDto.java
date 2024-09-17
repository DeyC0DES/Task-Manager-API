package com.taskmanager.taskmanager.dtos;

import java.time.LocalDate;

import com.taskmanager.taskmanager.enums.TaskStatsEnum;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TaskRecordDto(@NotNull @NotEmpty String taskname,
                            @NotNull TaskStatsEnum stats,
                            @NotNull LocalDate expireAt) {
    
}
