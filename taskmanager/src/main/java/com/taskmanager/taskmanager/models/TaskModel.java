package com.taskmanager.taskmanager.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.taskmanager.taskmanager.enums.TaskStatsEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tasks")
@Getter
@Setter
public class TaskModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;
    private UUID userId;
    private String taskname;
    private TaskStatsEnum stats;
    private LocalDate dateCreated;
    private LocalDate expireAt;
    

}
