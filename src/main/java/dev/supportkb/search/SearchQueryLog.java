package dev.supportkb.search;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "search_query_logs")
public class SearchQueryLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String query;

  @Column(name = "result_count", nullable = false)
  private long resultCount;

  @Column(name = "searched_at", nullable = false)
  private Instant searchedAt;

  protected SearchQueryLog() {}

  public SearchQueryLog(String query, long resultCount) {
    this.query = query;
    this.resultCount = resultCount;
  }

  @PrePersist
  void onCreate() {
    searchedAt = Instant.now();
  }

  public Long getId() {
    return id;
  }

  public String getQuery() {
    return query;
  }

  public long getResultCount() {
    return resultCount;
  }

  public Instant getSearchedAt() {
    return searchedAt;
  }
}
