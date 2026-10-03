# Step 18 production readiness

- Added the required Internet permission for Firebase connectivity.
- Added release/debug build-type configuration and conservative Firestore model keep rules.
- Added stricter Firestore rules: client writes cannot alter wallet, inventory, leaderboard, achievement, XP, win, rank, or notification creation data.
- Restricted quick chat to the predefined message set.
- Added local source validation and regression coverage from prior steps.

This project still requires a real Firebase project, `google-services.json`, a server-side trusted action processor for authoritative online dice/moves, and Android Studio/Gradle verification before release. The sandbox cannot run Gradle, the Android SDK, Firebase Emulator Suite, or device tests.
