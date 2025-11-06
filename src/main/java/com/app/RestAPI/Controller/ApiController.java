package com.app.RestAPI.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.app.RestAPI.Model.Task;
import com.app.RestAPI.Service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/tasks")
public class ApiController {

    Map<String, String> body = new HashMap<>();

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTask(@PathVariable long id) {
        Task result = taskService.getTask(id);

        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            body.put("message", "Task not found");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping
    public ResponseEntity<Object> saveTask(@Valid @RequestBody Task task) {
        taskService.saveTask(task);
        body.put("message", "Task saved");
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editTask(@PathVariable long id, @Valid @RequestBody Task task) {
        Task result = taskService.editTask(id, task);

        if (result != null) {
            body.put("message", "Task updated");
            return new ResponseEntity<>(body, HttpStatus.OK);
        } else {
            body.put("message", "Task not found");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable long id) {
        String result = taskService.deleteTask(id);

        if (result != null) {
            body.put("message", "Task deleted");
            return new ResponseEntity<>(body, HttpStatus.OK);
        } else {
            body.put("message", "Task not found");
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Invalid path");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleTypeMismatch(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Invalid field type");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
