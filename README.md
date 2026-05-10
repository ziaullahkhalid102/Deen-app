# Deen App

A modern Islamic social media platform built with Kotlin + Jetpack Compose (Material 3).

## Features

### Screens
- **Home Feed** - Facebook-style feed with stories row, post cards with like/comment/share
- **Shorts (Videos)** - TikTok-style full-screen vertical swipe video feed with Islamic content
- **Chat** - WhatsApp-style chat list with online indicators and unread badges
- **Profile** - User profile with banner, stats, and post grid
- **Search** - Search across users, posts, videos, and groups with tabbed results
- **Notifications** - Notification list for likes, comments, follows, shares
- **Create Post** - Post creation with photo/video/feeling/location options

### Design
- Material 3 Design with Islamic aesthetic (green, white, black accents)
- Light + Dark Mode support
- Smooth animations and transitions
- Rounded cards and modern spacing
- Clean, minimal, and responsive UI

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM
- **DI:** Hilt (Dagger)
- **Navigation:** Jetpack Navigation Compose
- **Image Loading:** Coil
- **Video:** ExoPlayer (Media3)
- **Backend:** Firebase
  - Authentication (Email + Google)
  - Firestore (posts, users, chats)
  - Firebase Storage (images/videos)
  - Firebase Messaging (push notifications)

## Setup

1. Clone the repository
2. Open in Android Studio (Hedgehog or newer)
3. Add your `google-services.json` from Firebase Console to `app/`
4. Sync Gradle and build
5. Run on device or emulator (API 26+)

## Project Structure

```
app/src/main/java/com/deenapp/
├── data/
│   ├── model/          # Data classes (User, Post, Story, etc.)
│   ├── repository/     # Repository pattern + sample data
│   └── service/        # Firebase messaging service
├── di/                 # Hilt dependency injection modules
├── ui/
│   ├── components/     # Reusable UI components
│   ├── navigation/     # Navigation graph
│   ├── screens/        # All app screens
│   │   ├── home/
│   │   ├── shorts/
│   │   ├── chat/
│   │   ├── profile/
│   │   ├── search/
│   │   ├── notifications/
│   │   └── createpost/
│   └── theme/          # Material 3 theme, colors, typography
├── viewmodel/          # ViewModels for each screen
├── DeenApplication.kt  # Hilt application class
└── MainActivity.kt     # Single activity entry point
```

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- Kotlin 2.1.0+
- Minimum SDK: 26 (Android 8.0)
- Target SDK: 35

## License

This project is for educational and personal use.
