# Flutter Screen Launch from Native Android App

This project demonstrates how to launch a **Flutter screen from a Native Android (Kotlin) app**, enabling seamless integration between native and Flutter modules.

## ✨ Features

- Launch Flutter screen from an existing native Android app
- Input product details in native Android (Kotlin) UI:
  - Product Name
  - Description
  - Image URL
  - Text Color
  - Text Size
  - Text Style (bold/italic/normal)
- Display product details beautifully on the Flutter side
- Use of `MethodChannel` to transfer structured data from Native to Flutter
- Optimized startup with `FlutterEngineCache` for fast and smooth transition

## 📱 How It Works

### 1. Native Android Side (Kotlin)
- A Kotlin-based UI form captures product data.
- A `MethodChannel` is set up to communicate with Flutter.
- `FlutterEngine` is initialized and cached using `FlutterEngineCache`.
- A `FlutterActivity` is launched to display the Flutter product description screen.

### 2. Flutter Side
- A simple Flutter UI listens for incoming data through `MethodChannel`.
- The data is parsed and shown using Flutter widgets with provided styles and values.

## 🔧 Tools & Technologies

- **Android (Kotlin)**
  - Jetpack Compose / XML (for native UI)
  - `MethodChannel`
  - `FlutterEngineCache`
  - `FlutterActivity`
- **Flutter (Dart)**
  - Stateless and Stateful Widgets
  - Platform channels for communication
  - UI customization with received parameters
