package com.hotkimho.realworldapi.domain;

/*
CREATE TABLE `articles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `title` varchar(128) NOT NULL,
  `description` varchar(128) NOT NULL,
  `body` varchar(128) NOT NULL,
  `favorite_count` int(11) NOT NULL DEFAULT 0,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_users_liked_articles` (`user_id`),
  KEY `idx_articles_deleted_at` (`deleted_at`),
  CONSTRAINT `fk_users_articles` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `fk_users_liked_articles` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
 */

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
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
    @Column(name = "user_id", updatable = false)
    private Long userId;

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

    // current_timestamp() is default value
    @NotNull()
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Article(Long userId, String title, String description, String body) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.body = body;
    }

    public void update(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;

        // print
        System.out.println("title : " + title);
        System.out.println("description : " + description);
        System.out.println("body : " + body);
    }
}

