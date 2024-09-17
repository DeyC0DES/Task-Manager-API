package com.taskmanager.taskmanager.dtos;

import com.taskmanager.taskmanager.enums.TaskStatsEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {
    
    private String taskname;
    private TaskStatsEnum stats;

}
