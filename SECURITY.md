# Security Policy

## Supported versions

Security fixes are applied to the latest commit on `main`. Older portfolio snapshots are not maintained.

## Reporting

Use GitHub private vulnerability reporting for this repository. Include the affected route, reproducible steps, impact, and a suggested mitigation if available.

Do not open a public issue containing exploit details, credentials, database contents, or personal data.

## Deployment warning

This project intentionally has no authentication. The content-management routes and write API must not be exposed directly to the public internet. Place a real deployment behind an authenticated reverse proxy, restrict network access, and enable TLS.

The example database password is for local development only. Production credentials belong in environment-specific secret storage.

## Content boundary

Article HTML is sanitized before storage and rendered only after sanitization. Changes to the jsoup safelist require dedicated security tests. Never bypass sanitization for imported or API-provided content.
