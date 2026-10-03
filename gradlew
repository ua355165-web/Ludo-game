#!/bin/sh
set -e
APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
DIST_URL="https://services.gradle.org/distributions/gradle-8.9-bin.zip"
CACHE="${GRADLE_USER_HOME:-$HOME/.gradle}/wrapper/dists/gradle-8.9-bin"
if command -v gradle >/dev/null 2>&1; then exec gradle "$@"; fi
if [ ! -x "$CACHE/gradle-8.9/bin/gradle" ]; then
  command -v curl >/dev/null 2>&1 || { echo "Gradle not found and curl is required to bootstrap Gradle 8.9." >&2; exit 1; }
  mkdir -p "$CACHE"
  tmp="$CACHE/gradle.zip"
  curl --fail --location --retry 2 "$DIST_URL" -o "$tmp"
  unzip -q -o "$tmp" -d "$CACHE"
  rm -f "$tmp"
fi
exec "$CACHE/gradle-8.9/bin/gradle" "$@"
