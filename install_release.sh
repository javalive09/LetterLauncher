#!/usr/bin/env bash

./gradlew clean
./gradlew assembleRelease

adb uninstall com.javalive09.letterlauncher
adb install app/build/outputs/apk/release/*.apk
adb shell am start -n com.javalive09.letterlauncher/.MainActivity