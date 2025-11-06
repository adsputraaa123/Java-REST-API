package com.app.RestAPI.Controller;

import com.app.RestAPI.Model.Task;
import com.app.RestAPI.Service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(ApiController.class)
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task sampleTask() {
        Task t = new Task();
        t.setId(1L);
        t.setTitle("Test Task");
        t.setDescription("Test Description");
        t.setCompleted(false);
        return t;
    }

    @Test
    void testGetAllTasks() throws Exception {
        List<Task> tasks = List.of(sampleTask());

        Mockito.when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void testGetTaskFound() throws Exception {
        Mockito.when(taskService.getTask(1L)).thenReturn(sampleTask());

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void testGetTaskNotFound() throws Exception {
        Mockito.when(taskService.getTask(99L)).thenReturn(null);

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }

    @Test
    void testSaveTask() throws Exception {
        Task newTask = sampleTask();

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task saved"));
    }

    @Test
    void testEditTaskFound() throws Exception {
        Task updated = sampleTask();
        updated.setTitle("Updated Title");

        Mockito.when(taskService.editTask(Mockito.eq(1L), any(Task.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task updated"));
    }

    @Test
    void testEditTaskNotFound() throws Exception {
        Mockito.when(taskService.editTask(Mockito.eq(99L), any(Task.class)))
                .thenReturn(null);

        mockMvc.perform(put("/api/tasks/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleTask())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }

    @Test
    void testDeleteTaskFound() throws Exception {
        Mockito.when(taskService.deleteTask(1L)).thenReturn("Task deleted");

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task deleted"));
    }

    @Test
    void testDeleteTaskNotFound() throws Exception {
        Mockito.when(taskService.deleteTask(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }
}
