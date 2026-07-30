package dev.supportkb.search;

import java.time.Instant;

public record NoResultSearchResponse(String query, Instant searchedAt) {}
