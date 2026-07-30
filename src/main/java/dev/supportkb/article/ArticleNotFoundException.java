package dev.supportkb.article;

public class ArticleNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ArticleNotFoundException(String identifier) {
    super("Article not found: " + identifier);
  }
}
