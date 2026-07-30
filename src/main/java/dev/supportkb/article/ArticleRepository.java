package dev.supportkb.article;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  boolean existsBySlug(String slug);

  boolean existsBySlugAndIdNot(String slug, Long id);

  Optional<Article> findBySlugAndStatus(String slug, ArticleStatus status);

  Page<Article> findByStatus(ArticleStatus status, Pageable pageable);

  Page<Article> findByStatusAndCategoryIgnoreCase(
      ArticleStatus status, String category, Pageable pageable);

  @Query(
      value =
          """
                    SELECT a.*
                    FROM articles a
                    WHERE a.status = 'PUBLISHED'
                      AND (:category IS NULL OR lower(a.category) = lower(:category))
                      AND (:tag IS NULL OR :tag = ANY(a.tags))
                    """,
      countQuery =
          """
                    SELECT count(*)
                    FROM articles a
                    WHERE a.status = 'PUBLISHED'
                      AND (:category IS NULL OR lower(a.category) = lower(:category))
                      AND (:tag IS NULL OR :tag = ANY(a.tags))
                    """,
      nativeQuery = true)
  Page<Article> filterPublished(
      @Param("category") String category, @Param("tag") String tag, Pageable pageable);

  @Query(
      value =
          """
                    SELECT a.*
                    FROM articles a,
                         websearch_to_tsquery('english', :query) search_query
                    WHERE a.status = 'PUBLISHED'
                      AND a.search_vector @@ search_query
                      AND (:category IS NULL OR lower(a.category) = lower(:category))
                      AND (:tag IS NULL OR :tag = ANY(a.tags))
                    ORDER BY ts_rank_cd(a.search_vector, search_query) DESC,
                             a.updated_at DESC
                    """,
      countQuery =
          """
                    SELECT count(*)
                    FROM articles a,
                         websearch_to_tsquery('english', :query) search_query
                    WHERE a.status = 'PUBLISHED'
                      AND a.search_vector @@ search_query
                      AND (:category IS NULL OR lower(a.category) = lower(:category))
                      AND (:tag IS NULL OR :tag = ANY(a.tags))
                    """,
      nativeQuery = true)
  Page<Article> searchPublished(
      @Param("query") String query,
      @Param("category") String category,
      @Param("tag") String tag,
      Pageable pageable);

  List<Article> findTop6ByStatusOrderByViewsDescUpdatedAtDesc(ArticleStatus status);

  List<Article> findTop4ByStatusAndCategoryIgnoreCaseAndIdNotOrderByViewsDescUpdatedAtDesc(
      ArticleStatus status, String category, Long id);

  @Query("select distinct a.category from Article a where a.status = :status order by a.category")
  List<String> findCategories(@Param("status") ArticleStatus status);

  @Query(
      value =
          """
                    SELECT DISTINCT tag
                    FROM articles a
                    CROSS JOIN LATERAL unnest(a.tags) AS tag
                    WHERE a.status = 'PUBLISHED'
                    ORDER BY tag
                    """,
      nativeQuery = true)
  List<String> findPublishedTags();
}
