package dev.supportkb.article;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "articles")
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 180)
  private String title;

  @Column(nullable = false, unique = true, length = 200)
  private String slug;

  @Column(nullable = false, length = 320)
  private String summary;

  @Column(nullable = false, columnDefinition = "text")
  private String content;

  @Column(nullable = false, length = 80)
  private String category;

  @JdbcTypeCode(SqlTypes.ARRAY)
  @Column(nullable = false, columnDefinition = "text[]")
  private List<String> tags = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ArticleStatus status = ArticleStatus.DRAFT;

  @Column(nullable = false)
  private long views;

  @Column(name = "helpful_count", nullable = false)
  private long helpfulCount;

  @Column(name = "not_helpful_count", nullable = false)
  private long notHelpfulCount;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @Version
  @Column(nullable = false)
  private long version;

  protected Article() {}

  public Article(
      String title,
      String slug,
      String summary,
      String content,
      String category,
      List<String> tags) {
    applyChanges(title, slug, summary, content, category, tags);
  }

  public void update(
      String title,
      String slug,
      String summary,
      String content,
      String category,
      List<String> tags) {
    applyChanges(title, slug, summary, content, category, tags);
  }

  private void applyChanges(
      String title,
      String slug,
      String summary,
      String content,
      String category,
      List<String> tags) {
    this.title = title;
    this.slug = slug;
    this.summary = summary;
    this.content = content;
    this.category = category;
    this.tags = new ArrayList<>(tags);
  }

  public void publish() {
    status = ArticleStatus.PUBLISHED;
  }

  public void unpublish() {
    status = ArticleStatus.DRAFT;
  }

  public void recordView() {
    views++;
  }

  public void recordFeedback(boolean helpful) {
    if (helpful) {
      helpfulCount++;
    } else {
      notHelpfulCount++;
    }
  }

  @PrePersist
  void onCreate() {
    Instant now = Instant.now();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  void onUpdate() {
    updatedAt = Instant.now();
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getSlug() {
    return slug;
  }

  public String getSummary() {
    return summary;
  }

  public String getContent() {
    return content;
  }

  public String getCategory() {
    return category;
  }

  public List<String> getTags() {
    return List.copyOf(tags);
  }

  public ArticleStatus getStatus() {
    return status;
  }

  public long getViews() {
    return views;
  }

  public long getHelpfulCount() {
    return helpfulCount;
  }

  public long getNotHelpfulCount() {
    return notHelpfulCount;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public long getVersion() {
    return version;
  }
}
