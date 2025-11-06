package com.app.RestAPI.Service;

import com.app.RestAPI.Model.Task;
import com.app.RestAPI.Repo.TaskRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepo taskRepo;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task();
        sampleTask.setId((long) 1);
        sampleTask.setTitle("Test Title");
        sampleTask.setDescription("Test Description");
        sampleTask.setCompleted(false);
    }

    @Test
    void getAllTasks() {
        List<Task> mockList = List.of(sampleTask);
        when(taskRepo.findAll()).thenReturn(mockList);

        List<Task> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getTitle());
        verify(taskRepo, times(1)).findAll();
    }

    @Test
    void getTaskFound() {
        when(taskRepo.findById((long) 1)).thenReturn(Optional.of(sampleTask));

        Task result = taskService.getTask((long) 1);

        assertNotNull(result);
        assertEquals((long) 1, result.getId());
    }

    @Test
    void getTaskNotFound() {
        when(taskRepo.findById((long) 1)).thenReturn(Optional.empty());

        Task result = taskService.getTask((long) 1);

        assertNull(result);
    }

    @Test
    void saveTask() {
        when(taskRepo.save(sampleTask)).thenReturn(sampleTask);

        Task result = taskService.saveTask(sampleTask);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        verify(taskRepo, times(1)).save(sampleTask);
    }

    @Test
    void editTaskFound() {
        Task updated = new Task();
        updated.setTitle("Updated Title");
        updated.setDescription("Updated Description");
        updated.setCompleted(true);

        when(taskRepo.findById((long) 1)).thenReturn(Optional.of(sampleTask));
        when(taskRepo.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        Task result = taskService.editTask((long) 1, updated);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertTrue(result.getCompleted());
        verify(taskRepo).save(any(Task.class));
    }

    @Test
    void editTaskNotFound() {
        when(taskRepo.findById((long) 1)).thenReturn(Optional.empty());

        Task result = taskService.editTask((long) 1, new Task());

        assertNull(result);
        verify(taskRepo, never()).save(any());
    }

    @Test
    void deleteTaskFound() {
        when(taskRepo.findById((long) 1)).thenReturn(Optional.of(sampleTask));

        String result = taskService.deleteTask((long) 1);

        assertEquals("Task deleted", result);
        verify(taskRepo, times(1)).deleteById((long) 1);
    }

    @Test
    void deleteTaskNFound() {
        when(taskRepo.findById((long) 1)).thenReturn(Optional.empty());

        String result = taskService.deleteTask((long) 1);

        assertNull(result);
        verify(taskRepo, never()).deleteById(any());
    }
}
