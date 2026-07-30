package dev.supportkb.article;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
@Validated
@Tag(name = "Articles")
public class ArticleApiController {

  private final ArticleService articleService;

  public ArticleApiController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping
  @Operation(summary = "Search or browse published articles")
  public PageResponse<ArticleSummaryResponse> browse(
      @RequestParam(required = false) String query,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String tag,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
      @RequestParam(defaultValue = "recent") String sort) {
    return articleService.browse(query, category, tag, page, size, sort);
  }

  @GetMapping("/{slug}")
  @Operation(summary = "Read a published article and increment its view counter")
  public ArticleDetailResponse get(@PathVariable String slug) {
    return articleService.readPublished(slug);
  }

  @PostMapping
  @Operation(summary = "Create a draft article")
  public ResponseEntity<ArticleResponse> create(@Valid @RequestBody ArticleRequest request) {
    ArticleResponse response = articleService.create(request);
    return ResponseEntity.created(URI.create("/api/articles/" + response.slug())).body(response);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Edit an article")
  public ArticleResponse update(@PathVariable Long id, @Valid @RequestBody ArticleRequest request) {
    return articleService.update(id, request);
  }

  @PostMapping("/{id}/publish")
  @Operation(summary = "Publish an article")
  public ArticleResponse publish(@PathVariable Long id) {
    return articleService.publish(id);
  }

  @PostMapping("/{id}/unpublish")
  @Operation(summary = "Return an article to draft status")
  public ArticleResponse unpublish(@PathVariable Long id) {
    return articleService.unpublish(id);
  }

  @PostMapping("/{id}/feedback")
  @Operation(summary = "Record Helpful or Not Helpful feedback")
  public ArticleResponse feedback(
      @PathVariable Long id, @Valid @RequestBody FeedbackRequest request) {
    return articleService.recordFeedback(id, request.helpful());
  }

  @GetMapping("/popular")
  @Operation(summary = "List the most viewed published articles")
  public List<ArticleSummaryResponse> popular() {
    return articleService.popular();
  }

  @GetMapping("/categories")
  @Operation(summary = "List published article categories")
  public List<String> categories() {
    return articleService.categories();
  }

  @GetMapping("/tags")
  @Operation(summary = "List published article tags")
  public List<String> tags() {
    return articleService.tags();
  }
}
