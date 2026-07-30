# Contributing

## Setup

1. Install Java 25 and Docker.
2. Run `./mvnw dependency:go-offline`.
3. Start from a focused branch based on `main`.

## Quality checks

Run before opening a pull request:

```bash
./mvnw spotless:check
./mvnw verify
docker compose build
```

Do not replace PostgreSQL integration tests with an in-memory database. Search vectors, arrays, GIN indexes, and query ranking must be tested against PostgreSQL.

## Commit convention

Use Conventional Commits:

```text
feat(search): add tag-aware ranking
fix(article): sanitize unsafe link protocols
test(api): cover publication workflow
docs: clarify database backup
```

## Database changes

- Add a new versioned Flyway migration; never edit a migration that may have run.
- Keep schema constraints aligned with Jakarta validation and domain behavior.
- Include a migration test or repository assertion for important changes.
- Explain forward and rollback considerations in the pull request.

## Pull requests

- Keep changes focused.
- Include tests for new behavior.
- Update OpenAPI and README examples when routes change.
- Never commit `.env`, database dumps, credentials, personal data, or build output.
- Use only fictional data and reserved domains in examples.
