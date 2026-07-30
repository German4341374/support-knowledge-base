# PostgreSQL search

## Search document

Flyway creates a `TSVECTOR` column and a trigger. The trigger combines:

```sql
SETWEIGHT(TO_TSVECTOR('english', title), 'A') ||
SETWEIGHT(TO_TSVECTOR('english', summary), 'B') ||
SETWEIGHT(TO_TSVECTOR('english', category), 'B') ||
SETWEIGHT(TO_TSVECTOR('english', ARRAY_TO_STRING(tags, ' ')), 'B') ||
SETWEIGHT(TO_TSVECTOR('english', content), 'C')
```

The trigger executes before insert or update, so vector state cannot lag behind article state.

## Query

The repository builds a query with:

```sql
websearch_to_tsquery('english', :query)
```

It matches the GIN-indexed vector and sorts by:

```sql
ts_rank_cd(search_vector, search_query) DESC, updated_at DESC
```

Title matches are more influential than matches buried in long content. The count query uses the same filters so pagination metadata remains correct.

## Supported behavior

- English stemming and stop words;
- quoted phrases and exclusions supported by web-search syntax;
- optional category and exact tag filters;
- published articles only;
- relevance ordering for non-empty queries;
- recent or popular ordering when browsing without a query.

## Limitations

- no fuzzy spelling correction;
- no synonyms dictionary;
- no multilingual configurations;
- no semantic similarity;
- HTML tags remain in the input to `to_tsvector`, although visible words are still indexed correctly.

These limitations are appropriate for the project scale and keep search fully local.

