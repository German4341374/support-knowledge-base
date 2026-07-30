package dev.supportkb.web;

import dev.supportkb.article.ArticleRequest;
import dev.supportkb.article.ArticleResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

public class ArticleForm {

  @NotBlank @Size(max = 180) private String title = "";

  @NotBlank @Size(max = 320) private String summary = "";

  @NotBlank @Size(max = 30_000) private String content = "";

  @NotBlank @Size(max = 80) private String category = "";

  @NotBlank @Size(max = 500) private String tags = "";

  public static ArticleForm from(ArticleResponse article) {
    ArticleForm form = new ArticleForm();
    form.title = article.title();
    form.summary = article.summary();
    form.content = article.content();
    form.category = article.category();
    form.tags = String.join(", ", article.tags());
    return form;
  }

  public ArticleRequest toRequest() {
    List<String> tagList =
        Arrays.stream(tags.split(",")).map(String::trim).filter(tag -> !tag.isBlank()).toList();
    return new ArticleRequest(title, summary, content, category, tagList);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }
}
