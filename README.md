# Mental Arithmetic — Kotlin Android App

An educational app for practicing arithmetic.

## Features

- START creates an exercise with two integer operands from 10 through 99 and a
  randomly selected operation: `*`, `/`, `-`, or `+`. A new exercise has a white
  background.
- For division, both operands are two-digit numbers and the quotient is always
  an integer (including 1).
- While solving an exercise, START is disabled; answer entry and CHECK are
  available.
- After checking, answer entry and CHECK are disabled, and START becomes
  available again.
- A correct answer turns the exercise green; an incorrect answer turns it red.
  A text message also shows the result and, for an incorrect answer, the correct
  answer.
- The screen displays the total number of completed exercises, correct and
  incorrect answers, and the percentage to two decimal places, for example
  `66.67%`.
- Negative answers and zero are supported. Empty input, a single minus sign, and
  numbers outside the Int range show a prompt: the check is not considered
  complete and the statistics do not change.
- On rotation, the exercise, entered text, result, background, button
  availability, input error, and statistics are preserved through
  `onSaveInstanceState`. Rotation is enabled, and the Activity is recreated as
  usual.
- The screen scrolls so that all elements remain accessible in landscape
  orientation and while the keyboard is open.

## Getting started

1. Clone the repository and open its root folder in Android Studio.
2. Select JDK 17 for Gradle and install Android SDK Platform 35 through the SDK
   Manager.
3. Wait for Gradle synchronization to finish.
4. Run the `app` configuration on an emulator or an Android 6.0 (API 23) device
   or later.

The project uses Kotlin 2.1.20, Android Gradle Plugin 8.9.2, and Gradle Wrapper
8.11.1. The interface is built programmatically with standard Android Views; no
additional UI libraries are required.

## Checks and APK

```sh
./gradlew testDebugUnitTest lintDebug assembleDebug
```

On Windows, use `gradlew.bat`. For command-line builds, set the SDK path through
`ANDROID_HOME` or `sdk.dir` in the local `local.properties` file (which is not
added to Git).

After the build, the APK is located at:
`app/build/outputs/apk/debug/app-debug.apk`.

GitHub Actions runs tests, lint, and the build on every push and pull request.
After a successful run, the APK is available under **Actions → Android build →
Artifacts → arithmetic-trainer-debug**.

## Project structure

- `MainActivity.kt` — interface, event handlers, statistics, and state saving.
- `Exercise.kt` — exercise model, answer calculation, and generator.
- `ExerciseTest.kt` — arithmetic and generator tests.
- `.github/workflows/android.yml` — automated checks and APK build.
