package com.app.RestAPI.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.RestAPI.Model.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {}
