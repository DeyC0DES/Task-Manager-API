package com.taskmanager.taskmanager.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskmanager.taskmanager.models.TaskModel;


public interface TaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByUserId(UUID id);
    List<TaskModel> findByTaskname(String taskname);
}
