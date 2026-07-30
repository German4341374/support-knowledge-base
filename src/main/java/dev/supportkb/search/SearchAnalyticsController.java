package dev.supportkb.search;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search-analytics")
public class SearchAnalyticsController {

  private final SearchQueryLogRepository repository;

  public SearchAnalyticsController(SearchQueryLogRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/no-results")
  public List<NoResultSearchResponse> noResultSearches() {
    return repository.findTop20ByResultCountOrderBySearchedAtDesc(0).stream()
        .map(item -> new NoResultSearchResponse(item.getQuery(), item.getSearchedAt()))
        .toList();
  }
}
