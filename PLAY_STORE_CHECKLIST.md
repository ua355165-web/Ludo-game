# Google Play Store preparation

## Release identity
- App name: Ludo Royale
- Application ID: `com.ludoroyale.app`
- Version name: `1.0.0`
- Version code: `1`
- Category: Games, Board (developer to confirm)
- Ads: none implemented
- Payments: none implemented
- Real-money features: none

## Required developer-supplied items
- Production Firebase `google-services.json` for the selected Firebase project
- Release keystore, alias, and passwords supplied through Gradle properties or CI secrets
- Public Privacy Policy URL
- Public Terms of Service URL
- Support email and support URL
- Data Safety form answers, based on the final Firebase configuration
- App access instructions if Play review needs authenticated online features
- Content rating questionnaire
- Target audience and ads declarations
- Store icon exports, feature graphic, and phone/tablet screenshots
- Short description and full description

## Build commands
```bash
./gradlew test
./gradlew assembleDebug
./gradlew assembleRelease
./gradlew bundleRelease
```

If signing properties are not supplied, release outputs remain unsigned and are for local testing only. Do not commit keystores, passwords, Firebase secrets, or `google-services.json` for a private production project.

## Current status
The project has release/debug build types, production package/version metadata, launcher icon resources, and an AAB-compatible Gradle configuration. APK/AAB generation still requires Android Studio or a configured Gradle + Android SDK environment.
