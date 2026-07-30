package dev.supportkb.web;

import dev.supportkb.article.ArticleDetailResponse;
import dev.supportkb.article.ArticleResponse;
import dev.supportkb.article.ArticleService;
import dev.supportkb.article.PageResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class KnowledgeBaseController {

  private final ArticleService articleService;

  public KnowledgeBaseController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("recent", articleService.browse(null, null, null, 0, 6, "recent").items());
    model.addAttribute("popular", articleService.popular());
    model.addAttribute("categories", articleService.categories());
    model.addAttribute("tags", articleService.tags().stream().limit(12).toList());
    return "home";
  }

  @GetMapping("/search")
  public String search(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String tag,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "recent") String sort,
      Model model) {
    int safePage = Math.max(0, page);
    PageResponse<?> results = articleService.browse(q, category, tag, safePage, 9, sort);
    model.addAttribute("results", results);
    model.addAttribute("q", q == null ? "" : q);
    model.addAttribute("category", category == null ? "" : category);
    model.addAttribute("tag", tag == null ? "" : tag);
    model.addAttribute("sort", sort);
    model.addAttribute("categories", articleService.categories());
    model.addAttribute("tags", articleService.tags());
    return "search";
  }

  @GetMapping("/articles/{slug}")
  public String article(@PathVariable String slug, Model model) {
    ArticleDetailResponse detail = articleService.readPublished(slug);
    model.addAttribute("article", detail.article());
    model.addAttribute("related", detail.relatedArticles());
    return "article";
  }

  @PostMapping("/articles/{id}/feedback")
  public String feedback(
      @PathVariable Long id,
      @RequestParam String slug,
      @RequestParam boolean helpful,
      RedirectAttributes redirectAttributes) {
    articleService.recordFeedback(id, helpful);
    redirectAttributes.addFlashAttribute("feedbackRecorded", true);
    return "redirect:/articles/" + slug;
  }

  @GetMapping("/manage/articles")
  public String manage(@RequestParam(defaultValue = "0") int page, Model model) {
    model.addAttribute("articles", articleService.listAll(Math.max(0, page), 20));
    return "manage-list";
  }

  @GetMapping("/manage/articles/new")
  public String createForm(Model model) {
    model.addAttribute("articleForm", new ArticleForm());
    model.addAttribute("editing", false);
    return "article-form";
  }

  @PostMapping("/manage/articles")
  public String create(
      @Valid @ModelAttribute ArticleForm articleForm,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("editing", false);
      return "article-form";
    }
    ArticleResponse article = articleService.create(articleForm.toRequest());
    redirectAttributes.addFlashAttribute("message", "Draft article created.");
    return "redirect:/manage/articles/" + article.id() + "/edit";
  }

  @GetMapping("/manage/articles/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    ArticleResponse article = articleService.getForManagement(id);
    model.addAttribute("articleForm", ArticleForm.from(article));
    model.addAttribute("article", article);
    model.addAttribute("editing", true);
    return "article-form";
  }

  @PostMapping("/manage/articles/{id}")
  public String update(
      @PathVariable Long id,
      @Valid @ModelAttribute ArticleForm articleForm,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("article", articleService.getForManagement(id));
      model.addAttribute("editing", true);
      return "article-form";
    }
    articleService.update(id, articleForm.toRequest());
    redirectAttributes.addFlashAttribute("message", "Article updated.");
    return "redirect:/manage/articles/" + id + "/edit";
  }

  @PostMapping("/manage/articles/{id}/publish")
  public String publish(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    articleService.publish(id);
    redirectAttributes.addFlashAttribute("message", "Article published.");
    return "redirect:/manage/articles/" + id + "/edit";
  }

  @PostMapping("/manage/articles/{id}/unpublish")
  public String unpublish(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    articleService.unpublish(id);
    redirectAttributes.addFlashAttribute("message", "Article returned to draft.");
    return "redirect:/manage/articles/" + id + "/edit";
  }
}
