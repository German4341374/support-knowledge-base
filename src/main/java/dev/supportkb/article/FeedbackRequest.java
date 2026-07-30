package dev.supportkb.article;

import jakarta.validation.constraints.NotNull;

public record FeedbackRequest(@NotNull Boolean helpful) {}
