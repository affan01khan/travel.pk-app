# Travel.pk Android App

Native Android application built with Kotlin and Jetpack Compose, with a separate Node.js REST API backend.

## Repository structure

```text
.
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/pk/travel/app/MainActivity.kt
│       └── res/drawable/
├── backend/
│   ├── src/
│   │   ├── data.js
│   │   ├── db.js
│   │   ├── routes.js
│   │   └── server.js
│   ├── .env.example
│   ├── .gitignore
│   ├── Dockerfile
│   ├── package.json
│   └── README.md
├── .github/workflows/android.yml
├── build.gradle.kts
└── settings.gradle.kts
```

## Android app

The Android client provides native screens for flight search entry, destinations, packages, trips, help and other travel services. Live flight availability and booking are not simulated: they require an authorised Travel.pk or supplier integration.

## Backend API

See [backend/README.md](backend/README.md) for installation, environment settings, API routes and deployment notes.

The backend currently provides health, destination, sample package and customer enquiry endpoints. Flight search and booking lookup intentionally return a service-unavailable response until Travel.pk/provider API access is supplied. The Android client has not yet been wired to a deployed backend URL.

## Build

Open the repository in Android Studio or run:

```bash
gradle assembleDebug
```

The GitHub Actions workflow also builds a debug APK on pushes to `main`.
