package dev.supportkb.article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.supportkb.search.SearchQueryLog;
import dev.supportkb.search.SearchQueryLogRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

  @Mock private ArticleRepository articleRepository;
  @Mock private SearchQueryLogRepository searchQueryLogRepository;
  @Mock private SlugService slugService;
  @Mock private HtmlSanitizer htmlSanitizer;

  private ArticleService articleService;

  @BeforeEach
  void setUp() {
    articleService =
        new ArticleService(articleRepository, searchQueryLogRepository, slugService, htmlSanitizer);
  }

  @Test
  void createsSanitizedDraftWithNormalizedTags() {
    ArticleRequest request =
        new ArticleRequest(
            "  VPN setup  ",
            "  Connect remotely.  ",
            "<script>bad()</script><p>Safe</p>",
            " Network ",
            List.of("VPN", " remote-work ", "VPN"));
    when(slugService.uniqueForCreate(request.title())).thenReturn("vpn-setup");
    when(htmlSanitizer.sanitize(request.content())).thenReturn("<p>Safe</p>");
    when(articleRepository.save(any(Article.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ArticleResponse response = articleService.create(request);

    assertThat(response.title()).isEqualTo("VPN setup");
    assertThat(response.slug()).isEqualTo("vpn-setup");
    assertThat(response.content()).isEqualTo("<p>Safe</p>");
    assertThat(response.tags()).containsExactly("remote-work", "vpn");
    assertThat(response.status()).isEqualTo(ArticleStatus.DRAFT);
  }

  @Test
  void recordsAQueryWhenSearchHasNoResults() {
    when(articleRepository.searchPublished(any(String.class), any(), any(), any(Pageable.class)))
        .thenReturn(Page.empty());

    PageResponse<ArticleSummaryResponse> response =
        articleService.browse("unknown device error", null, null, 0, 10, "recent");

    assertThat(response.totalElements()).isZero();
    verify(searchQueryLogRepository).save(any(SearchQueryLog.class));
  }

  @Test
  void doesNotRecordSuccessfulSearchAsNoResult() {
    Article article =
        new Article(
            "VPN setup", "vpn-setup", "Connect.", "<p>Steps</p>", "Network", List.of("vpn"));
    when(articleRepository.searchPublished(any(String.class), any(), any(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(article)));

    PageResponse<ArticleSummaryResponse> response =
        articleService.browse("vpn", null, null, 0, 10, "recent");

    assertThat(response.totalElements()).isOne();
  }
}
