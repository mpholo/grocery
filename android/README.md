# Grocery Android App

A native Android client for the `grocery` Spring Boot application. It lets you:

- Browse the **monthly groceries** and open a month's item list.
- **Toggle** each grocery item as *added to basket* (the `purchased` flag) via a checkbox.
- **Edit** an item's **quantity** and **unit price** from a dialog.

It talks to the backend over the JSON REST API in `grocery-web`
(`/api/v1/monthlygroceries/summaries`, `/api/v1/groceryitems`).

## Tech stack

| Area            | Choice                                   |
|-----------------|------------------------------------------|
| Language        | Kotlin                                   |
| UI              | Jetpack Compose + Material 3             |
| Architecture    | MVVM (`ViewModel` + `StateFlow`)         |
| Networking      | Retrofit 2 + OkHttp + Gson               |
| Navigation      | Navigation-Compose                       |
| Min / target SDK| 24 / 35                                  |

Key versions are pinned in [`gradle/libs.versions.toml`](gradle/libs.versions.toml)
(AGP 8.9.0, Kotlin 2.4.10, Compose BOM 2026.09.00, Retrofit 2.11.0). If Android
Studio prompts to adjust the AGP/Gradle pairing on first sync, accept it.

## Project layout

```
app/src/main/java/com/mpholo/project/grocery/android/
├── MainActivity.kt
├── data/
│   ├── api/GroceryApi.kt          # Retrofit interface
│   ├── api/NetworkModule.kt       # Retrofit/OkHttp setup (service locator)
│   ├── model/Models.kt            # DTOs mirroring the backend JSON
│   └── GroceryRepository.kt       # single data access point
└── ui/
    ├── GroceryNavHost.kt          # months -> items navigation
    ├── UiState.kt / StateContent.kt
    ├── months/                    # month list screen + ViewModel
    ├── items/                     # item list, basket toggle, edit dialog + ViewModel
    └── theme/
```

## Running it

### 1. Start the backend
From the repository root (needs **JDK 11** — Lombok 1.18.12 does not build on JDK 21):

```bash
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64 ./mvnw -pl grocery-web -am spring-boot:run
```

The app defaults to the `h2` profile and listens on `http://localhost:8080`.
The new API endpoints are open (added to the Spring Security `PUBLIC_MATCHERS`).

### 2. Point the app at the backend
`BASE_URL` is a `BuildConfig` field set in [`app/build.gradle.kts`](app/build.gradle.kts).

- **Emulator:** the default `http://10.0.2.2:8080/` reaches your host machine.
- **Physical device:** change it to your machine's LAN IP, e.g. `http://192.168.1.20:8080/`,
  and add that host to [`res/xml/network_security_config.xml`](app/src/main/res/xml/network_security_config.xml)
  (cleartext HTTP is only allowed for the listed dev hosts).

### 3. Build / run the app
Open the `android/` folder in **Android Studio** and run the `app` configuration,
or from the command line once the Gradle wrapper exists:

```bash
cd android
./gradlew installDebug
```

> **Gradle wrapper:** this scaffold ships `gradle/wrapper/gradle-wrapper.properties`
> but not the binary `gradle-wrapper.jar`. Android Studio generates it automatically
> on first open. From the CLI without Android Studio, run `gradle wrapper` once (with a
> local Gradle install) to materialise `gradlew` and the jar.

## API contract used

| Method | Path                                         | Purpose                                   |
|--------|----------------------------------------------|-------------------------------------------|
| GET    | `/api/v1/monthlygroceries/summaries`         | list months (flat, no nested items)       |
| GET    | `/api/v1/groceryitems?monthlyGroceryId={id}` | list a month's items                      |
| PATCH  | `/api/v1/groceryitems/{id}`                  | update `quantity`, `actualPrice`, `purchased` (any subset) |
