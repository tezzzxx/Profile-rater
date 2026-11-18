# Contracts between the services

All external calls go through the gateway: `http://127.0.0.1:8080`.
JSON uses camelCase. Errors are returned as `{ "error": "description" }`.

| Service | Port (inside Docker) | Database |
|---|---|---|
| gateway | 8080 | – |
| profile | 8001 | `mongo_profile` (db `profiles`) |
| rating | 8002 | `mongo_rating` (db `ratings`) |

## REST

### profile
| Method | Path | Response |
|---|---|---|
| POST | `/profiles` | `201 {id, ownerCode}` |
| GET | `/profiles/next?raterId=...` | `200` profile with `bioVersion` A or B, or `204` if none left |
| GET | `/profiles/{id}` | `200` profile, or `404` |
| GET | `/profiles/results/{ownerCode}` | `200` results, or `404` |
| DELETE | `/profiles/{ownerCode}` | `204`, or `404` |

### rating
| Method | Path | Response |
|---|---|---|
| POST | `/ratings` | `201`, `400` (invalid score), `404` (unknown profile), `409` (already rated), `503` (profile down) |
| GET | `/ratings/profile/{profileId}` | `200` list of `{comment, bioVersion, createdAt}`, newest first |

## Events (Kafka)

Values are sent as JSON strings. The key is `profileId`, so all events for the same profile go to the same partition.

### RatingCreated
| | |
|---|---|
| Topic | `rating-created` |
| Producer | rating |
| Consumer | profile, group `profile` |

```json
{
  "ratingId": "6af43458dfb90a8ec8bb048d",
  "profileId": "6af433ee1d00824755ee1652",
  "raterId": "d34b18ee-577c-4f58-a119-e5223519da69",
  "bioVersion": "A",
  "picRating": 4,
  "bioRating": 3,
  "overallRating": 5,
  "createdAt": "2026-11-11T09:24:40Z"
}
```

The comment is not included. Profile fetches the comments from rating with REST when the results page is shown.

### ProfileDeleted
| | |
|---|---|
| Topic | `profile-deleted` |
| Producer | profile |
| Consumer | rating, group `rating` |

```json
{
  "profileId": "6af433ee1d00824755ee1652",
  "deletedAt": "2026-11-16T12:00:00Z"
}
```

Rating deletes all ratings for the profile.