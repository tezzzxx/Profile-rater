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

# Lage mer senere