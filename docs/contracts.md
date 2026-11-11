# Overview

Service:
 - gateway
   - Port: 8080 
 - user
   - Port: 8001
   - Database: mongo_user
 - rating
   - Port: 8002
   - Database: mongo_rating
 - matching
   - Port: 8003
   - Database: mongo_matching

All external calls go through the gateway: `http://localhost:8080`.
JSON uses camelCase. IDs are strings (Mongo ObjectId).
Errors are returned as: `{ "error": "description" }`

## Events (Kafka)

### RatingCreated
Sent by rating every time a new rating is saved.

| | |
|---|---|
| Topic | `rating-created` |
| Producer | rating |
| Consumer | profile, group-id `profile-service` |
| Key | `profileId` |
| Value | JSON sent as a plain string |

```json
{
  "ratingId": "6af43458dfb90a8ec8bb048d",
  "profileId": "6af433ee1d00824755ee1652",
  "raterId": "rater-10",
  "picRating": 4,
  "bioRating": 3,
  "overAllRating": 5,
  "createdAt": "2026-11-11T09:24:40"
}
```

| Field | Type | Meaning |
|---|---|---|
| `ratingId` | string | ID of the saved rating in rating's database |
| `profileId` | string | The profile that was rated |
| `raterId` | string | Anonymous ID of the person who rated (from the browser) |
| `picRating` | int 1–5 | Score for the photo |
| `bioRating` | int 1–5 | Score for the bio |
| `overAllRating` | int 1–5 | Overall score |
| `createdAt` | ISO date-time | When the rating was given |

The comment is not included. Profile fetches comments from rating via REST
(`GET /ratings/profile/{profileId}`) when showing the results page.