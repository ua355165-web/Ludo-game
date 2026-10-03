# Ludo Royale cloud Android build

Workflow name: **Ludo Royale Android Cloud Build**

Workflow file: `.github/workflows/android-build.yml`

## GitHub setup

1. Push this project to a GitHub repository without committing `google-services.json`, keystores, passwords, or private keys.
2. In **Settings > Secrets and variables > Actions**, add these repository secrets:
   - `GOOGLE_SERVICES_JSON`: the complete contents of the real Firebase `app/google-services.json` file.
   - `KEYSTORE_BASE64`: base64-encoded release keystore.
   - `KEYSTORE_PASSWORD`: release keystore password.
   - `KEY_ALIAS`: release key alias.
   - `KEY_PASSWORD`: release key password.
3. Run **Actions > Ludo Royale Android Cloud Build > Run workflow**, or push to `main`/`master`.

## What the workflow does

The GitHub Ubuntu runner installs Java 17, Android SDK platform 35, build tools 35.0.0, and bootstraps Gradle 8.9 through the checked-in `gradlew` script. It detects the existing `:app` module, runs unit tests, and always attempts the debug APK.

If `GOOGLE_SERVICES_JSON` is absent, the workflow reports that production Firebase configuration is missing and continues with the debug build. If the JSON is present but invalid, the workflow fails clearly. If all four signing secrets are present, it generates and uploads the signed release APK and Play Store AAB. If signing secrets are incomplete, release outputs are skipped rather than falsely reported as successful.

## Downloading artifacts

Open the successful workflow run and download:

- `ludo-royale-debug-apk`: debug APK for testing.
- `ludo-royale-release-apk`: signed release APK, only when signing secrets are configured.
- `ludo-royale-release-aab`: signed Play Store bundle, only when signing secrets are configured.

Expected files inside the project runner:

- `app/build/outputs/apk/debug/app-debug.apk`
- `app/build/outputs/apk/release/app-release.apk`
- `app/build/outputs/bundle/release/app-release.aab`

A debug APK is for local/device testing. The AAB is the preferred Google Play upload artifact and must be signed with the developer's private release key.
