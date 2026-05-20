# Paillardes — development commands
# Requires: JDK 17, Android SDK, adb

export JAVA_HOME := env("JAVA_HOME", "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home")
export ANDROID_HOME := env("ANDROID_HOME", env("HOME", "") + "/Library/Android/sdk")

gradlew := "cd Paillarde && ./gradlew --no-daemon -q"

# List available recipes
default:
    @just --list

# Run Android lint
lint:
    {{ gradlew }} lintDebug
    @echo "Lint report: Paillarde/app/build/reports/lint-results-debug.html"

# Build debug APK
bug:
    {{ gradlew }} assembleDebug
    @echo "Debug APK: Paillarde/app/build/outputs/apk/debug/app-debug.apk"

# Build signed release bundle (AAB) for Play Store
release:
    {{ gradlew }} bundleRelease
    @echo "Release bundle: Paillarde/app/build/outputs/bundle/release/app-release.aab"

# Install debug APK on connected device (USB or WiFi — use `just connect` first for WiFi)
deploy: bug
    #!/usr/bin/env bash
    tid=$(adb devices -l | grep 'transport_id:' | head -1 | grep -oE 'transport_id:[0-9]+' | grep -oE '[0-9]+')
    adb -t "$tid" install -r Paillarde/app/build/outputs/apk/debug/app-debug.apk

# Connect to a device over WiFi (e.g. `just connect 192.168.1.42`)
connect ip port="5555":
    adb connect {{ ip }}:{{ port }}

# Run unit tests (JVM)
test:
    {{ gradlew }} testDebugUnitTest

# Clean build artifacts
clean:
    {{ gradlew }} clean

# Bump versionName and auto-increment versionCode
# Usage: just bump 1.1
bump version:
    #!/usr/bin/env bash
    set -euo pipefail
    f="Paillarde/app/build.gradle"
    current_code=$(grep -E '^\s+versionCode\s' "$f" | grep -oE '[0-9]+')
    new_code=$((current_code + 1))
    sed -i '' "s/versionCode ${current_code}/versionCode ${new_code}/" "$f"
    sed -i '' 's/versionName "[^"]*"/versionName "{{ version }}"/' "$f"
    echo "Bumped: versionName={{ version }}, versionCode=${new_code}"
