package com.dev.moim.domain.moim.entity;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.enums.TodoStatus;
import com.dev.moim.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'IN_PROGRESS'")
    @Column(nullable = false)
    private TodoStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moim_id")
    private Moim moim;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTodo> userTodoList = new ArrayList<>();

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoImage> todoImageList = new ArrayList<>();

    public void updateWriter(User writer) {
        this.writer = writer;
    }

    public void updateTodo(
            String title,
            String content,
            LocalDateTime dueDate,
            List<TodoImage> newImageList
    ) {
        this.title = title;
        this.content = content;
        this.dueDate = dueDate;
        this.todoImageList.clear();
        this.todoImageList.addAll(newImageList);
    }

    public void updateStatus(TodoStatus status) {
        this.status = status;
    }
}
