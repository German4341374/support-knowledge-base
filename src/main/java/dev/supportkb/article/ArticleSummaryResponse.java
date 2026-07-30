package dev.supportkb.article;

import java.time.Instant;
import java.util.List;

public record ArticleSummaryResponse(
    Long id,
    String title,
    String slug,
    String summary,
    String category,
    List<String> tags,
    ArticleStatus status,
    long views,
    long helpfulCount,
    long notHelpfulCount,
    Instant updatedAt) {

  public static ArticleSummaryResponse from(Article article) {
    return new ArticleSummaryResponse(
        article.getId(),
        article.getTitle(),
        article.getSlug(),
        article.getSummary(),
        article.getCategory(),
        article.getTags(),
        article.getStatus(),
        article.getViews(),
        article.getHelpfulCount(),
        article.getNotHelpfulCount(),
        article.getUpdatedAt());
  }
}
