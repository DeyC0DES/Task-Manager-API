package com.taskmanager.taskmanager.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskmanager.taskmanager.models.TaskModel;
import com.taskmanager.taskmanager.repositories.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskModel save(TaskModel model) {
        taskRepository.save(model);
        return model;
    }

    public List<TaskModel> findAll() {
        return taskRepository.findAll();
    }

    public List<TaskModel> findByUserId(UUID id) {
        return taskRepository.findByUserId(id);
    }

    public List<TaskModel> findByTaskname(String taskname) {
        return taskRepository.findByTaskname(taskname);
    }

    public void delete(TaskModel model) {
        taskRepository.delete(model);
    }

}
