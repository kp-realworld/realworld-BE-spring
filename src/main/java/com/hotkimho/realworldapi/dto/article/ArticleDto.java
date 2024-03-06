package com.hotkimho.realworldapi.dto.article;

import com.hotkimho.realworldapi.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleDto {

        private Long articleId;
        private Long userId;
        private UserDto Author;
        private String title;
        private String description;
        private String body;
        private int favoriteCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ArticleDto() {
        }

        public ArticleDto(Long id, Long userId, String title, String description, String body, int favoriteCount, String createdAt, String updatedAt, String deletedAt) {
            this.id = id;
            this.userId = userId;
            this.title = title;
            this.description = description;
            this.body = body;
            this.favoriteCount = favoriteCount;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.deletedAt = deletedAt;
        }
}
