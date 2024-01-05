package com.k1per32.TaskManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Data
@ToString(exclude = {"userList", "comments"})
@Table(name = "tasks", schema = "keycloak")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private String status;
    private String priority;
    @Column(name = "author_id")
    private String authorId;
    @Column(name = "performer_id")
    private String performerId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_comment")
    private List<Comment> comments;

    @JsonIgnore
    @ManyToMany(mappedBy = "taskList")
    private List<User> userList;

    public void addCommentToTask(Comment comment){
        if( comments == null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

}

