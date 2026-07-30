# Operations guide

## Health

```bash
curl --fail http://localhost:8080/actuator/health
```

The endpoint reports only aggregate status. PostgreSQL connectivity is included through Spring Boot Actuator.

## Logs

Production console logs use structured JSON:

```bash
docker compose logs --follow app
```

Do not log article bodies, database passwords, or request headers.

## Backup

Create a compressed PostgreSQL backup:

```bash
mkdir -p backups
docker compose exec -T database \
  pg_dump --format=custom --no-owner \
  --username support_kb support_kb > backups/support-kb.dump
```

Restore into an empty development database:

```bash
docker compose down --volumes
docker compose up --detach database
docker compose exec -T database \
  pg_restore --clean --if-exists --no-owner \
  --username support_kb --dbname support_kb < backups/support-kb.dump
docker compose up --detach app
```

Confirm the actual database and user values from the environment before running backup or restore.

## Troubleshooting

### Application remains unhealthy

1. Run `docker compose ps`.
2. Inspect `docker compose logs database app`.
3. Confirm PostgreSQL is healthy.
4. Check that application credentials match the database container.
5. Inspect Flyway errors before retrying.

### Search returns no expected article

1. Confirm the article is `PUBLISHED`.
2. Search a distinctive word from the title.
3. Check category and tag filters.
4. Inspect the migration history with `flyway_schema_history`.
5. Confirm `search_vector` is populated and the trigger exists.

### Flyway checksum mismatch

Never edit an applied migration. Restore the original file and add a new migration for the correction.

## Destroy local data

```bash
docker compose down --volumes --remove-orphans
```

This permanently deletes the local PostgreSQL volume. It does not affect backup files.

