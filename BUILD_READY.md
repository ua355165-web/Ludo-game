# Build-ready instructions

## Toolchain
- JDK 17
- Android SDK platform 35 and build tools installed
- Gradle 8.9, bootstrapped by `./gradlew` or `gradlew.bat`
- Android Studio Ladybug or newer is recommended

## Firebase
Place the real Firebase file at `app/google-services.json`. It is intentionally not included. Keep it private and do not commit it. The app compiles against Firebase SDKs without this file; Firebase runtime configuration is required for Auth/Firestore production use.

## Signing
Put these in `~/.gradle/gradle.properties` or CI secrets, never source control:

```properties
releaseKeystorePath=/secure/path/ludo-royale-release.jks
releaseKeystorePassword=...
releaseKeyAlias=...
releaseKeyPassword=...
```

## Outputs
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`
- Release AAB: `app/build/outputs/bundle/release/app-release.aab`

## Commands

```bash
./gradlew clean
./gradlew test
./gradlew assembleDebug
./gradlew assembleRelease
./gradlew bundleRelease
```

The included GitHub Actions workflow runs tests and debug/release APK/AAB tasks on a hosted Android runner. It requires signing secrets for a signed release artifact and a real Firebase configuration for online production behavior.
