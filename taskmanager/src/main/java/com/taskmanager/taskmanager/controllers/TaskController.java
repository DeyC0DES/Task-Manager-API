package com.taskmanager.taskmanager.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmanager.dtos.TaskDto;
import com.taskmanager.taskmanager.dtos.TaskRecordDto;
import com.taskmanager.taskmanager.models.TaskModel;
import com.taskmanager.taskmanager.models.UserModel;
import com.taskmanager.taskmanager.services.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("task")
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody @Valid TaskRecordDto body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel model = (UserModel) authentication.getPrincipal();

        List<TaskModel> tasks = this.taskService.findByTaskname(body.taskname());
        List<TaskModel> userTask = tasks.stream()
                .filter(task -> task.getUserId().equals(model.getId()))
                .collect(Collectors.toList());

        if (userTask.size() >= 1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This task already exist");
        }

        TaskModel task = new TaskModel();
        task.setUserId(model.getId());
        task.setTaskname(body.taskname());
        task.setStats(body.stats());
        task.setDateCreated(LocalDate.now());
        task.setExpireAt(body.expireAt());

        return ResponseEntity.status(HttpStatus.OK).body(this.taskService.save(task));
    }

    @GetMapping("/get")
    public ResponseEntity<List<TaskModel>> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel model = (UserModel) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(this.taskService.findByUserId(model.getId()));
    }

    @GetMapping("/get/{taskname}")
    public ResponseEntity<Object> findByTaskname(@PathVariable(value="taskname") String taskname) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel model = (UserModel) authentication.getPrincipal();

        List<TaskModel> tasks = this.taskService.findByTaskname(taskname);

        List<TaskModel> userTasks = tasks.stream()
                .filter(task -> task.getUserId().equals(model.getId()))
                .collect(Collectors.toList());

        if (userTasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(userTasks);
    }

    @PutMapping("/update/{taskname}")
    public ResponseEntity<Object> updateTask(@PathVariable(value="taskname") String taskname,
                                             @RequestBody TaskDto body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel model = (UserModel) authentication.getPrincipal();

        List<TaskModel> tasks = this.taskService.findByTaskname(taskname);
        List<TaskModel> userTasks = tasks.stream()
                .filter(task -> task.getUserId().equals(model.getId()))
                .collect(Collectors.toList());

        if (userTasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        userTasks.forEach(task -> {
            TaskModel newTask = task;
            newTask.setTaskname(body.getTaskname());
            newTask.setStats(body.getStats());
            this.taskService.save(newTask);
        });

        return ResponseEntity.status(HttpStatus.OK).body(userTasks.size() + " Task(s) changed!");
    }

    @DeleteMapping("delete/{taskname}")
    public ResponseEntity<Object> register(@PathVariable(value="taskname") String taskname) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel model = (UserModel) authentication.getPrincipal();

        List<TaskModel> task = this.taskService.findByTaskname(taskname);

        List<TaskModel> userTasks = task.stream()
                .filter(tasks -> tasks.getUserId().equals(model.getId()))
                .collect(Collectors.toList());

        userTasks.forEach(tasks -> this.taskService.delete(tasks));
        return ResponseEntity.status(HttpStatus.OK).body("Task(s) founded: "+userTasks.size()+"\n\n"+"Task(s) deleted successfully");
    }

}
