package dev.supportkb.article;

import dev.supportkb.search.SearchQueryLog;
import dev.supportkb.search.SearchQueryLogRepository;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final SearchQueryLogRepository searchQueryLogRepository;
  private final SlugService slugService;
  private final HtmlSanitizer htmlSanitizer;

  public ArticleService(
      ArticleRepository articleRepository,
      SearchQueryLogRepository searchQueryLogRepository,
      SlugService slugService,
      HtmlSanitizer htmlSanitizer) {
    this.articleRepository = articleRepository;
    this.searchQueryLogRepository = searchQueryLogRepository;
    this.slugService = slugService;
    this.htmlSanitizer = htmlSanitizer;
  }

  @Transactional
  public ArticleResponse create(ArticleRequest request) {
    Article article =
        new Article(
            cleanText(request.title()),
            slugService.uniqueForCreate(request.title()),
            cleanText(request.summary()),
            htmlSanitizer.sanitize(request.content()),
            cleanText(request.category()),
            normalizeTags(request.tags()));
    return ArticleResponse.from(articleRepository.save(article));
  }

  @Transactional
  public ArticleResponse update(Long id, ArticleRequest request) {
    Article article = getById(id);
    String slug =
        article.getTitle().equals(request.title().trim())
            ? article.getSlug()
            : slugService.uniqueForUpdate(request.title(), id);
    article.update(
        cleanText(request.title()),
        slug,
        cleanText(request.summary()),
        htmlSanitizer.sanitize(request.content()),
        cleanText(request.category()),
        normalizeTags(request.tags()));
    return ArticleResponse.from(article);
  }

  @Transactional
  public ArticleResponse publish(Long id) {
    Article article = getById(id);
    article.publish();
    return ArticleResponse.from(article);
  }

  @Transactional
  public ArticleResponse unpublish(Long id) {
    Article article = getById(id);
    article.unpublish();
    return ArticleResponse.from(article);
  }

  @Transactional
  public ArticleDetailResponse readPublished(String slug) {
    Article article =
        articleRepository
            .findBySlugAndStatus(slug, ArticleStatus.PUBLISHED)
            .orElseThrow(() -> new ArticleNotFoundException(slug));
    article.recordView();
    List<ArticleSummaryResponse> related =
        articleRepository
            .findTop4ByStatusAndCategoryIgnoreCaseAndIdNotOrderByViewsDescUpdatedAtDesc(
                ArticleStatus.PUBLISHED, article.getCategory(), article.getId())
            .stream()
            .map(ArticleSummaryResponse::from)
            .toList();
    return new ArticleDetailResponse(ArticleResponse.from(article), related);
  }

  @Transactional(readOnly = true)
  public ArticleResponse getForManagement(Long id) {
    return ArticleResponse.from(getById(id));
  }

  @Transactional
  public ArticleResponse recordFeedback(Long id, boolean helpful) {
    Article article = getById(id);
    if (article.getStatus() != ArticleStatus.PUBLISHED) {
      throw new ArticleNotFoundException(id.toString());
    }
    article.recordFeedback(helpful);
    return ArticleResponse.from(article);
  }

  @Transactional
  public PageResponse<ArticleSummaryResponse> browse(
      String query, String category, String tag, int page, int size, String sort) {
    PageRequest pageRequest = PageRequest.of(page, size, requestedSort(sort));
    String normalizedQuery = trimToNull(query);
    String normalizedCategory = trimToNull(category);
    String normalizedTag = trimToNull(tag);

    Page<Article> results =
        normalizedQuery == null
            ? articleRepository.filterPublished(normalizedCategory, normalizedTag, pageRequest)
            : articleRepository.searchPublished(
                normalizedQuery, normalizedCategory, normalizedTag, PageRequest.of(page, size));
    if (normalizedQuery != null && results.getTotalElements() == 0) {
      searchQueryLogRepository.save(
          new SearchQueryLog(limit(normalizedQuery, 200), results.getTotalElements()));
    }
    return PageResponse.from(results.map(ArticleSummaryResponse::from));
  }

  @Transactional(readOnly = true)
  public PageResponse<ArticleSummaryResponse> listAll(int page, int size) {
    Page<ArticleSummaryResponse> articles =
        articleRepository
            .findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt")))
            .map(ArticleSummaryResponse::from);
    return PageResponse.from(articles);
  }

  @Transactional(readOnly = true)
  public List<ArticleSummaryResponse> popular() {
    return articleRepository
        .findTop6ByStatusOrderByViewsDescUpdatedAtDesc(ArticleStatus.PUBLISHED)
        .stream()
        .map(ArticleSummaryResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<String> categories() {
    return articleRepository.findCategories(ArticleStatus.PUBLISHED);
  }

  @Transactional(readOnly = true)
  public List<String> tags() {
    return articleRepository.findPublishedTags();
  }

  private Article getById(Long id) {
    return articleRepository
        .findById(id)
        .orElseThrow(() -> new ArticleNotFoundException(id.toString()));
  }

  private List<String> normalizeTags(List<String> tags) {
    return tags.stream()
        .filter(Objects::nonNull)
        .map(String::trim)
        .filter(tag -> !tag.isBlank())
        .map(tag -> tag.toLowerCase(Locale.ROOT))
        .map(tag -> limit(tag, 40))
        .collect(
            java.util.stream.Collectors.collectingAndThen(
                java.util.stream.Collectors.toCollection(LinkedHashSet::new),
                values -> values.stream().sorted(Comparator.naturalOrder()).limit(12).toList()));
  }

  private Sort requestedSort(String sort) {
    if ("popular".equalsIgnoreCase(sort)) {
      return Sort.by(Sort.Direction.DESC, "views").and(Sort.by(Sort.Direction.DESC, "updatedAt"));
    }
    return Sort.by(Sort.Direction.DESC, "updatedAt");
  }

  private String cleanText(String value) {
    return value.trim().replaceAll("\\s+", " ");
  }

  private String trimToNull(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }

  private String limit(String value, int maximumLength) {
    return value.length() <= maximumLength ? value : value.substring(0, maximumLength);
  }
}
