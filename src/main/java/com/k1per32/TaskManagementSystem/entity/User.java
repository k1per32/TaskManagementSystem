package com.k1per32.TaskManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "user_entity", schema = "keycloak")
@ToString(exclude = {"comments", "taskList"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String email;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_comment")
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "user_tasks",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id")}
    )
    @JsonIgnore
    private List<Task> taskList;

    public void addCommentToUser(Comment comment){
        if( comments == null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }
}

