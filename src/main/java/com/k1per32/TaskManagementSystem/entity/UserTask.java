package com.k1per32.TaskManagementSystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "user_tasks", schema = "keycloak")
public class UserTask implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "task_id")
    private Integer taskId;
}

