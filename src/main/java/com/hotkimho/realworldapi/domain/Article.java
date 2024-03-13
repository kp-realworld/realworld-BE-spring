package com.hotkimho.realworldapi.domain;


import jakarta.persistence.*;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE articles SET deleted_at = current_timestamp() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull()
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotNull()
    @Column(name = "title")
    private String title;

    @NotNull()
    @Column(name = "description")
    private String description;

    @NotNull()
    @Column(name = "body")
    private String body;

    // set default value to 0
    @Column(name = "favorite_count")
    private int favoriteCount = 0;

    @Column(name = "created_at" , updatable = false, nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Article(String title, String description, String body, User user) {
        this.title = title;
        this.description = description;
        this.body = body;
        this.user = user;
    }

    public Article(Long articleId) {
        this.id = articleId;
    }

    public void update(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

    @PreUpdate
    @PrePersist
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ArticleTag> articleTags;
}

