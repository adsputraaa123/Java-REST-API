package com.app.RestAPI.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.RestAPI.Model.Task;
import com.app.RestAPI.Repo.TaskRepo;

@Service
public class TaskService {

    Map<String, String> body = new HashMap<>();

    @Autowired
    private TaskRepo taskRepo;

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public Task getTask(long id) {
        return taskRepo.findById(id).orElse(null);
    }

    public Task saveTask(Task task) {
        return taskRepo.save(task);
    }

    public Task editTask(long id, Task task) {
        Task existedTask = taskRepo.findById(id).isPresent() ? taskRepo.findById(id).get() : null;

        if (existedTask != null) {
            existedTask.setTitle(task.getTitle());
            existedTask.setDescription(task.getDescription());
            existedTask.setCompleted(task.getCompleted());
            return taskRepo.save(existedTask);
        } else {
            return null;
        }
    }

    public String deleteTask(long id) {
        Task existedTask = taskRepo.findById(id).isPresent() ? taskRepo.findById(id).get() : null;

        if (existedTask != null) {
            taskRepo.deleteById(id);
            return "Task deleted";
        } else {
            return null;
        }
    }

}
