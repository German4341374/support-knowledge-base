# Repository Guide

## Scope

Keep this project a focused support knowledge base. Do not add users, roles, comments, WYSIWYG editing, email, or an external search engine without an approved architecture decision.

## Boundaries

- Controllers handle HTTP and view models.
- `ArticleService` owns article workflows and transaction boundaries.
- `ArticleRepository` owns PostgreSQL queries.
- Flyway owns schema and seed changes.
- Render article HTML only after `HtmlSanitizer` has processed it.
- Test PostgreSQL-specific behavior with Testcontainers, never H2.

## Working agreement

- Use English in source, comments, configuration, documentation, and commits.
- Use Conventional Commits.
- Do not commit secrets, real support tickets, personal data, local databases, or build output.
- Run Spotless and the full Maven verification before submitting changes.
