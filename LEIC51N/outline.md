
## Week 1
### 14/09/2023 - Course introduction

* Syllabus, teaching methodology and bibliography.
  * Evaluation
  * Resources

For reference:
* [Download Android Studio & App Tools](https://developer.android.com/studio)
* [Android API Levels](https://apilevels.com/)
* [SDK Platform release notes | Android Developers](https://developer.android.com/studio/releases/platforms)

### 15/09/2023 - Practical class
* Goal: Install Android Studio and create a new project (the *Hello Android* app) using Android Studio's wizard.
* Challenge: From the miriad of questions that may arise, elect the two most relevant ones for refining your mental model of the Android platform. The professor will use them as talking points in the next class.

## Week 2
### 21/09/2023 - Building Android UIs with Jetpack Compose (part 1)
* Android development, introduction:
  * Identifying Android Studio project artifacts
* Activity: introduction to the `Activity` class and its lifecycle.
* Jetpack Compose: revisions
  * @Composable functions: properties and constraints
  * Stateless vs. stateful @Composables
  * Introduction to the catalog of @Composables
    * Elementar @Composables: `Text`, `Button`
    * Layout @Composables: `Column`, `Row`, `Box`
  * State management in @Composable functions
    * `remember` - for memoizing values
    * `mutableStateOf` - for observable mutable state


For reference:
* [Thinking in Compose](https://developer.android.com/jetpack/compose/mental-model)
* [Compose layout basics](https://developer.android.com/jetpack/compose/layouts/basics)
* [State in Jetpack Compose](https://developer.android.com/jetpack/compose/state)
* [Testing Jetpack Compose UIs](https://developer.android.com/jetpack/compose/testing)


### 22/09/2023 - Practical class
* Goal: Exercise building a UI with Jetpack Compose
* Challenge 1: Build an application for counting persons entering and leaving a room. The application is comprised of a single screen that displays the current number of persons in the room and has two buttons, one for incrementing the count and another for decrementing it.
* Challenge 1.5: Try to mimic the following design on your application https://henryegloff.com/simple-counter/
* Project: Build the screen for displaying information about the application's authors. For now, focus on the screen's layout. No behaviour is required at this time.


## Week 3
### 28/09/2023 - Building Android UIs with Jetpack Compose (part 2)
* Jetpack Compose, continued
  * Effects in @Composable functions
    * `LaunchedEffect` - for launching coroutines as a consequence of a composition
    * `rememberCoroutineScope` - for obtaining a scope to launch coroutines in event handlers
* Automated tests in Android Studio (introduction)
  * Instrumented tests: testing a Compose UI

For reference:
* [Introduction to the Activity component](https://developer.android.com/guide/components/activities/intro-activities)
* [State and Jetpack Compose](https://developer.android.com/jetpack/compose/state)
* [Side-effects in Compose](https://developer.android.com/jetpack/compose/side-effects)
* [Kotlin coroutines on Android](https://developer.android.com/kotlin/coroutines)
* [Test Apps on Android](https://developer.android.com/training/testing)
  * [Test doubles in Android](https://developer.android.com/training/testing/fundamentals/test-doubles)
  * [Mockk, a mocking library for Kotlin](https://mockk.io/)
* [Challenge 1 solution](https://github.com/isel-leic-pdm/2324i/tree/main/challenges)
* [Loading images](https://developer.android.com/jetpack/compose/graphics/images/loading)
* [Coil](https://github.com/coil-kt/coil#jetpack-compose)
* [Corrotinas Kotlin, com Pedro Félix](https://www.youtube.com/watch?v=K_fqNQz3UoU&list=PL8XxoCaL3dBhrsR7Sf4tVxOLCdrzfWTvG&index=1)
* [Corrotinas: PC LEIC43D 2122v - Aula 16](https://www.youtube.com/watch?v=hFWVNIkciwY&list=PL8XxoCaL3dBiv-3pHZLbFGYsQiJa9X73o&index=16)

### 29/09/2023 - Practical class
* Goal: Exercise building a UI with Jetpack Compose using state hoisting and effects
* Challenge 2: Build a StopWatch application, for counting time. The application has a screen with the current count and buttons to start, stop and restart the count.
* Challenge 2.5: Try to mimic the following design on your application https://www.behance.net/gallery/40534207/StopWatch-Day84100-My-UIUX-Free-SketchApp-Challenge
* Project: Build the screen for playing the game (with partial implementation of the behavior, that is, moves are made but the rules of the game are not yet applied). Create automatic tests for the screen and for the game logic that is implemented. There's still no navigation between the screens.
