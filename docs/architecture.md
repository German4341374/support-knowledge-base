# Architecture decisions

## Modular monolith

The UI and REST API live in one Spring Boot process and share the same application service. This minimizes deployment and operational overhead for a small support catalog while keeping clear package boundaries.

A separate frontend service would add networking, versioning, and deployment complexity without improving the current user scenarios.

## PostgreSQL as the search engine

Search is a core data concern, so PostgreSQL stores and queries the search document:

- a trigger updates `search_vector` in the same transaction as the article;
- GIN indexes accelerate matching;
- weighted vectors promote titles and summaries;
- `websearch_to_tsquery` provides familiar query syntax;
- `ts_rank_cd` calculates relevance.

This avoids synchronization failures and operational cost from an external search cluster. The trade-off is less sophisticated typo tolerance and language analysis.

## Sanitized HTML

Articles accept a documented HTML subset. jsoup removes scripts, event attributes, images, styles, and unsafe URL schemes before content reaches the database.

This provides useful structured support content without adding a WYSIWYG editor or a large Markdown rendering stack. Raw input must never be rendered directly.

## Explicit publication

New articles start as `DRAFT`. Only `PUBLISHED` articles appear in public queries and search vectors are filtered by status at query time. Unpublishing preserves the article and counters.

There is no user identity or approval chain in this project. A production deployment must protect management routes externally.

## Optimistic locking

The `version` column prevents one stale edit from silently overwriting another. The API exposes the version for diagnostics, though a future version should support an `If-Match` workflow.

## Search analytics

Zero-result queries are stored separately from articles. This gives maintainers a concrete documentation backlog without adding an analytics platform.

Search terms may be sensitive. Operators should document retention and prohibit credentials or personal data in searches.

