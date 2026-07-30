package dev.supportkb.article;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HtmlSanitizerTest {

  private HtmlSanitizer sanitizer;

  @BeforeEach
  void setUp() {
    sanitizer = new HtmlSanitizer();
  }

  @Test
  void keepsUsefulSupportFormatting() {
    String result =
        sanitizer.sanitize("<h2>Steps</h2><ol><li>Restart VPN</li></ol><p>Try again.</p>");

    assertThat(result).contains("<h2>Steps</h2>", "<li>Restart VPN</li>", "<p>Try again.</p>");
  }

  @Test
  void removesScriptsAndEventHandlers() {
    String result =
        sanitizer.sanitize("<p onclick=\"steal()\">Safe text</p><script>alert('x')</script>");

    assertThat(result).isEqualTo("<p>Safe text</p>");
  }

  @Test
  void removesUnsafeLinks() {
    String result = sanitizer.sanitize("<a href=\"javascript:alert(1)\">Open</a>");

    assertThat(result).isEqualTo("<a>Open</a>");
  }

  @Test
  void removesImages() {
    assertThat(sanitizer.sanitize("<p>Text</p><img src=\"https://example.test/a.png\">"))
        .isEqualTo("<p>Text</p>");
  }
}
