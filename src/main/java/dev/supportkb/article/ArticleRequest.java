package dev.supportkb.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record ArticleRequest(
    @NotBlank @Size(max = 180) String title,
    @NotBlank @Size(max = 320) String summary,
    @NotBlank @Size(max = 30_000) String content,
    @NotBlank @Size(max = 80) String category,
    @NotEmpty @Size(max = 12) List<@NotBlank @Size(max = 40) String> tags) {}
