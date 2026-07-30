package dev.supportkb.article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SlugServiceTest {

  @Mock private ArticleRepository articleRepository;

  private SlugService slugService;

  @BeforeEach
  void setUp() {
    slugService = new SlugService(articleRepository);
  }

  @Test
  void createsLowercaseHyphenatedSlug() {
    assertThat(slugService.uniqueForCreate("Fix VPN Authentication Failed!"))
        .isEqualTo("fix-vpn-authentication-failed");
  }

  @Test
  void removesDiacritics() {
    assertThat(slugService.uniqueForCreate("Résumé Wi-Fi Setup")).isEqualTo("resume-wi-fi-setup");
  }

  @Test
  void fallsBackWhenTitleContainsNoAsciiCharacters() {
    assertThat(slugService.uniqueForCreate("東京")).isEqualTo("article");
  }

  @Test
  void addsIncrementingSuffixForDuplicateSlugs() {
    when(articleRepository.existsBySlug("vpn-setup")).thenReturn(true);
    when(articleRepository.existsBySlug("vpn-setup-2")).thenReturn(true);

    assertThat(slugService.uniqueForCreate("VPN setup")).isEqualTo("vpn-setup-3");
  }

  @Test
  void excludesCurrentArticleDuringUpdate() {
    when(articleRepository.existsBySlugAndIdNot("printer-offline", 42L)).thenReturn(false);

    assertThat(slugService.uniqueForUpdate("Printer offline", 42L)).isEqualTo("printer-offline");
  }
}
