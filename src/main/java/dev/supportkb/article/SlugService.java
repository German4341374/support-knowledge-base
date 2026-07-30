package dev.supportkb.article;

import java.text.Normalizer;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class SlugService {

  private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");
  private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9]+");
  private static final Pattern EDGE_HYPHENS = Pattern.compile("(^-+|-+$)");

  private final ArticleRepository articleRepository;

  public SlugService(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  public String uniqueForCreate(String title) {
    return makeUnique(baseSlug(title), articleRepository::existsBySlug);
  }

  public String uniqueForUpdate(String title, Long articleId) {
    return makeUnique(
        baseSlug(title), candidate -> articleRepository.existsBySlugAndIdNot(candidate, articleId));
  }

  String baseSlug(String title) {
    String normalized = Normalizer.normalize(title, Normalizer.Form.NFD).toLowerCase(Locale.ROOT);
    String withoutDiacritics = DIACRITICS.matcher(normalized).replaceAll("");
    String withHyphens = NON_ALPHANUMERIC.matcher(withoutDiacritics).replaceAll("-");
    String slug = EDGE_HYPHENS.matcher(withHyphens).replaceAll("");
    return slug.isBlank() ? "article" : slug;
  }

  private String makeUnique(String base, Predicate<String> exists) {
    String candidate = base;
    int suffix = 2;
    while (exists.test(candidate)) {
      candidate = base + "-" + suffix++;
    }
    return candidate;
  }
}
