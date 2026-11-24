# Deploying the grocery backend to the dliving.co.za server (Docker)

The backend runs as a container on the existing `my-network`, uses the existing
`grocery_db` Postgres container, and is published at **https://grocery.dliving.co.za/**
through the existing nginx `web` container. DNS (`grocery` A record → 102.68.97.47)
is already in place.

Run everything below **on the VPS**, from the directory that holds your
`docker-compose.yml` (the one with `backend`, `web`, `grocery_db`).

---

## 1. Put the grocery repo on the server

Clone/copy this repo next to your other build contexts, as `./grocery`:

```bash
git clone <your-grocery-repo-url> grocery
# result: ./rental-rooms  ./rentalroomsapps  ./postgresql  ./grocery
```

## 2. Confirm the `grocery` database exists in Postgres

The `postgres` profile connects to `grocery_db:5432/grocery` (the container is named
`grocery_db`; the database it needs is named `grocery`). This database already exists
on the server, so there is nothing to create. Confirm with:

```bash
docker exec -it grocery_db psql -U postgres -c "\l" | grep grocery
```

If for some reason it is missing, create it once:

```bash
docker exec -it grocery_db psql -U postgres -c "CREATE DATABASE grocery;"
```

Hibernate (`ddl-auto=update`) creates the tables on first start. The app begins
**empty** — add months via the Android app, or ask to have the 265-line `data.sql`
seed wired in.

## 3. Add the grocery service to docker-compose.yml

Merge the service from `ops/compose.grocery.snippet.yml` into your existing
`docker-compose.yml` under `services:`. Then build it:

```bash
docker compose build grocery_backend
docker compose up -d grocery_backend
docker compose logs -f grocery_backend   # watch for "Started GroceryApplication"
```

At this point the app is reachable *inside* the network as `grocery_backend:8080`
but not yet from the internet.

## 4. nginx: HTTP block first (for the TLS cert)

Add **only the port-80 server block** from `ops/nginx-grocery.conf` to the nginx
config in `./rentalroomsapps`, then rebuild and restart `web`:

```bash
docker compose build web
docker compose up -d web
```

## 5. Issue the TLS certificate (free, Let's Encrypt)

Using your existing letsencrypt volumes (webroot challenge):

```bash
docker run --rm \
  -v "$(pwd)/letsencrypt/conf:/etc/letsencrypt" \
  -v "$(pwd)/letsencrypt/www:/var/www/certbot" \
  certbot/certbot certonly --webroot -w /var/www/certbot \
  -d grocery.dliving.co.za \
  --email mpholo.leboea@gmail.com --agree-tos --no-eff-email
```

You should see `Successfully received certificate`. Files land in
`./letsencrypt/conf/live/grocery.dliving.co.za/`, which `web` sees at
`/etc/letsencrypt/live/...`.

## 6. nginx: add the HTTPS block

Now add the **port-443 server block** from `ops/nginx-grocery.conf`, rebuild and
restart `web`:

```bash
docker compose build web
docker compose up -d web
```

Verify:

```bash
curl -s https://grocery.dliving.co.za/api/v1/monthlygroceries/summaries | head
```

## 7. Android release build

`app/build.gradle.kts` already points the release build at
`https://grocery.dliving.co.za/`. Build the release APK:

```bash
cd android
./gradlew assembleRelease     # (configure a signing key for a Play/store build)
```

---

## Security note (please read)

`/api/**` is currently `permitAll` in `SecurityConfig` (it was opened so the app
could reach it without a login). Once this is on the public internet, anyone with
the URL can read and modify your lists. Options, cheapest first:

1. **nginx Basic auth** on the grocery server block (see the commented block in
   `nginx-grocery.conf`) + an OkHttp `Authorization` header in the app. Small change.
2. **API key** header checked by a Spring filter + sent by the app.
3. **Full auth** (token/JWT) in the app — most work.

Say which you want and I'll implement it.

## Renewals

The cert auto-renews with however you renew the `www` cert today (same volumes).
If you run certbot renew on a timer, `grocery.dliving.co.za` is picked up
automatically; nginx just needs a reload after renewal.
