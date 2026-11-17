# Profile Rater

This project was made for the exam in PG3402 Microservices

Profile rater is a playful webapplication which let you post your dating profile
and receive feedback. This is for all the dating-app users who want to optimize your 
dating profile to get more interaction. Also for you who want to help others with
constructive feedback to better their profile. it is anonymous, you can write multiple
bios (So you can choose the best one) and all you need is your owner code to keep track
of everything.

## Architecture

![Architecture](docs/Architecture.png)

- **frontend:** React app served by nginx. Talks only to the gateway.
- **gateway:** the only entry point. Routes `/profiles/**` to profile and `/ratings/**` to rating.
- **profile:** creates, shows and deletes profiles. Keeps score totals and calculates results.
- **rating:** stores ratings and comments. Makes sure a browser rates a profile only once.
- **Kafka:** sends events between profile and rating.
- **Consul:** service discovery and central configuration.
- **MongoDB:** one database per service, `mongo_profile` and `mongo_rating`.

## Communication

**Synchronous(REST):**
  - rating → profile: before a rating is saved, rating checks that the profile exists
  - profile → rating: the result page gets comments for a profile from rating

**Asynchronous(Kafka):**
  - `RatingCreated`(rating → profile): profile updates the score totals
  - `ProfileDeleted`(profile → rating): rating deletes all ratings for the deleted profile

## Load balancing & configuration

- profile and rating run with 2 instances each.
- Gateway uses `lb://profile` and `lb://rating`, looks up the instances in Consul
  and spreads the calls between them. If one stops the other one takes over.
- Central configuration is stored in Consul KV. The`consul-init` container puts the values in at startup. The values can be changed in Consul while the system is running without a rebuild.
  - `profile.min-ratings`: how many ratings a profile needs before results are shown
  - `rating.max-score`: the highest score you can give when reviewing.

## Robustness
- Profile does not depend on rating to show results. The scores are stored in profile, so only the comments are missing if rating does not answer.
- Kafka may deliver an event twice. Profile keeps a list of who has rated each profile, so duplicates are ignored.
- Every service has a health check, and Consul only routes to healthy instances.

## How to run

### Requirements

- Docker Desktop
- Git
- For local development: Java 21 and Node 22

> **Windows users:** Use `127.0.0.1` instead of `localhost`. `localhost` can resolve to IPv6 (`::1`) and give "Connection reset". In PowerShell, use `curl.exe` instead of `curl`.

### Startup procedure (Docker)

**1. Clone the repository**

```bash
git clone https://github.com/tezzzxx/Profile-rater.git
cd Profile-rater
```

**2. Build and start everything**

```bash
docker compose up --build -d
```

**3. Running**

- Frontend: http://127.0.0.1:5173 
- Gateway: http://127.0.0.1:8080 
- Consul UI: http://127.0.0.1:8500 
- Swagger (rating): http://127.0.0.1:8080/rating-docs/swagger-ui.html 

**4. Checks**

- **Containers**: all should be `running` or `healthy`:
  ```bash
  docker compose ps
  ```
- **Consul UI** (http://127.0.0.1:8500): `gateway`, `profile` (2 instances) and `rating` (2 instances) should all be green.
- **Gateway health:**
  ```bash
  curl.exe http://127.0.0.1:8080/actuator/health
  # {"status":"UP"}
  ```
- **Create a test profile** through the gateway:
  ```bash
  curl.exe -i -X POST http://127.0.0.1:8080/profiles -H "Content-Type: application/json" -d "@profile/profile.json"
  # HTTP/1.1 201 Created  {"id":"...","ownerCode":"..."}
  ```

**5. Stop**

```bash
docker compose down # stop     
docker compose down -v # stop and delete all data
```

### Startup procedure (local development)

**1. Start the infrastructure in Docker**

```bash
docker compose up -d consul kafka mongo_profile mongo_rating
```

**2. Start the backend**

Run `GatewayApplication`, `ProfileApplication` and `RatingProfileApplication` from IntelliJ. The `local` Spring profile is active by default and uses `localhost` for Kafka, MongoDB and Consul.

**3. Start the frontend**

```bash
cd frontend
npm install
npm run dev
```

### Build and test with Maven

```bash
cd rating
./mvnw clean package     # build
./mvnw test              # integration tests (Docker must be running)
```
The same `./mvnw clean package` works in `gateway/` and `profile/`. On Windows use `.\mvnw`.

### Test data

- `profile/profile.json`: example body for `POST /profiles`
- `rating/rating.json`: example body for `POST /ratings` (replace `profileId` with an existing id first)

```bash
curl.exe -i -X POST http://127.0.0.1:8080/ratings -H "Content-Type: application/json" -d "@rating/rating.json"
```



