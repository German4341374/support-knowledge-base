package dev.supportkb.article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class HtmlSanitizer {

  private final Safelist safelist =
      Safelist.relaxed()
          .removeTags("img")
          .addTags("h2", "h3", "kbd")
          .addAttributes("code", "class")
          .addProtocols("a", "href", "http", "https", "mailto")
          .preserveRelativeLinks(true);

  public String sanitize(String unsafeHtml) {
    Document.OutputSettings outputSettings =
        new Document.OutputSettings().prettyPrint(false).charset("UTF-8");
    return Jsoup.clean(unsafeHtml, "", safelist, outputSettings);
  }
}
