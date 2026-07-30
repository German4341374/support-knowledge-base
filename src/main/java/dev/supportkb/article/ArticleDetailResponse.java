package dev.supportkb.article;

import java.util.List;

public record ArticleDetailResponse(
    ArticleResponse article, List<ArticleSummaryResponse> relatedArticles) {}
