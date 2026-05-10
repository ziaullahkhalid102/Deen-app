---
name: deen-app-build-test
description: How to build, test, and develop the Deen App Android project (Kotlin + Jetpack Compose).
---

## Environment Setup

Requires Android SDK with `platforms;android-35` and `build-tools;35.0.0`.

```bash
export ANDROID_HOME=$HOME/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools
```

Requires `google-services.json` from Firebase Console placed in `app/` for full builds on device. Not required for compilation checks.

## Build Commands

```bash
# Kotlin compilation check (fastest verification)
./gradlew compileDebugKotlin --no-daemon

# Full debug APK build
./gradlew assembleDebug --no-daemon
# APK output: app/build/outputs/apk/debug/app-debug.apk
```

## Known Issues

- Google Auth libraries (`google-auth-library-oauth2-http`, `google-auth-library-credentials`) produce duplicate `META-INF/INDEX.LIST` files. Resolved with packaging exclusions in `app/build.gradle.kts`.
- Deprecation warnings for `Icons.Default.ArrowBack` and `Icons.Default.Send` — should use `Icons.AutoMirrored.Filled.ArrowBack` and `Icons.AutoMirrored.Filled.Send` in future.

## Project Structure

- **Package**: `com.deenapp`
- **Architecture**: MVVM + Hilt DI + Jetpack Navigation
- **Screens**: 13 (Splash, Welcome, ProfileSetup, Home, Shorts, Create, Chat, ChatDetail, Profile, Notifications, Search, Settings)
- **ViewModels**: 8 (Auth, Home, Profile, Chat, Shorts, Search, Notifications + inline)
- **Min SDK**: 26, **Target SDK**: 35
- **Kotlin**: 2.1.0, **Gradle**: 8.11.1

## Testing

No unit tests or instrumented tests are configured. Testing is build verification only (no emulator available on CI/VM). For UI testing, use Android Studio with an emulator (API 26+).

Verification checklist:
1. `compileDebugKotlin` succeeds with 0 error lines
2. `assembleDebug` produces APK > 1MB
3. All Screen routes in `DeenNavigation.kt` have matching `composable()` entries
4. All data model fields referenced by UI composables exist
