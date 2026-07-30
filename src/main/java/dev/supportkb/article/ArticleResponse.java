package dev.supportkb.article;

import java.time.Instant;
import java.util.List;

public record ArticleResponse(
    Long id,
    String title,
    String slug,
    String summary,
    String content,
    String category,
    List<String> tags,
    ArticleStatus status,
    long views,
    long helpfulCount,
    long notHelpfulCount,
    Instant createdAt,
    Instant updatedAt,
    long version) {

  public static ArticleResponse from(Article article) {
    return new ArticleResponse(
        article.getId(),
        article.getTitle(),
        article.getSlug(),
        article.getSummary(),
        article.getContent(),
        article.getCategory(),
        article.getTags(),
        article.getStatus(),
        article.getViews(),
        article.getHelpfulCount(),
        article.getNotHelpfulCount(),
        article.getCreatedAt(),
        article.getUpdatedAt(),
        article.getVersion());
  }
}
