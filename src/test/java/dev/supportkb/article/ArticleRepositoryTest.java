package dev.supportkb.article;

import static org.assertj.core.api.Assertions.assertThat;

import dev.supportkb.AbstractPostgresIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ArticleRepositoryTest extends AbstractPostgresIntegrationTest {

  @Autowired private ArticleRepository articleRepository;

  @Test
  void flywaySeedContainsAtLeastTwentyArticles() {
    assertThat(articleRepository.count()).isGreaterThanOrEqualTo(20);
  }

  @Test
  void fullTextSearchFindsVpnTroubleshootingByRelevance() {
    Page<Article> result =
        articleRepository.searchPublished(
            "VPN authentication failed", null, null, PageRequest.of(0, 10));

    assertThat(result.getTotalElements()).isGreaterThan(0);
    assertThat(result.getContent().getFirst().getSlug()).isEqualTo("fix-vpn-authentication-failed");
  }

  @Test
  void fullTextSearchDoesNotReturnDrafts() {
    Page<Article> result =
        articleRepository.searchPublished(
            "remote support consent", null, null, PageRequest.of(0, 10));

    assertThat(result).isEmpty();
  }

  @Test
  void categoryAndTagFiltersCanBeCombined() {
    Page<Article> result =
        articleRepository.filterPublished("Network", "wi-fi", PageRequest.of(0, 10));

    assertThat(result.getContent())
        .isNotEmpty()
        .allMatch(article -> article.getCategory().equals("Network"))
        .allMatch(article -> article.getTags().contains("wi-fi"));
  }

  @Test
  void publishedTagsAreUniqueAndSorted() {
    assertThat(articleRepository.findPublishedTags())
        .contains("vpn", "windows", "printer")
        .doesNotHaveDuplicates()
        .isSorted();
  }
}
