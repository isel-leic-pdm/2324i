
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

## Week 4
### 05/10/2023 - National Holiday
### 06/10/2023 - Building Android UIs with Jetpack Compose (wraping up) 
* Considerations on software design in Android using Jetpack Compose
  * Clear separation of concerns between UI and domain logic
* Domain logic is comprised of:
  * Domain data (immutable)
  * Domain operations (pure functions)
* The UI is comprised of independent screens
  * Each screen is comprised of:
    * An Activity that hosts the screen
    * A @Composable function that's responsible for building the screen's UI: i.e. the screen's root composable
    * The screen's UI is comprised of @Composable functions that build the screen's details
* UI state and behaviour management is made by:
  * Pushing down state to the leaf @Composable functions
  * Propagating events up to the screen's root @Composable function
* Domain data is published to the UI as observable mutable state (i.e. using `mutableStateOf`)
* We will soon include another element in this design: the view model. Stay tuned.
* Application resources: internationalization and localization

For reference:
* [Architecting your Compose UI](https://developer.android.com/jetpack/compose/architecture)
* [Guide to application architecture](https://developer.android.com/jetpack/guide)
* [Application resources](https://developer.android.com/guide/topics/resources/providing-resources)
* [Challenge 2 solution](https://github.com/isel-leic-pdm/2324i/tree/main/challenges)
* [Resources in Compose](https://developer.android.com/jetpack/compose/resources)

Video lecture (in Portuguese):
* [Android @ ISEL - 2023 - (04) Jetpack Compose (considerações de desenho)](https://www.youtube.com/watch?v=4C2v3Bv54WM&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=4)


### 06/10/2023 - Practical class
* Goal: Exercise the creation of solutions ensure a clear separation of concerns between UI and domain logic
* Project: Complete the implementation of the screen for playing the game, along with the respective domain logic. The game is played locally, not involving communication with either the Web API (option C) or Firestore (option B). In the implementation, take into account that, subsequently,  communication will occur during the game. For now, there is still no navigation between the application's screens.

## Week 5
### 12/10/2023 - Android UX: navigating between screens
* UI navigation in Android
  * Navigation between activities
  * User experience: _user tasks_ and _back stack_
* Intents: 
  * Explicit - for navigating between activities in the same application
  * Implicit - for navigating between activities in different applications
  * Intent filters as a means for declaring the activities that can handle an intent

For reference:
* [Tasks and the back stack](https://developer.android.com/guide/components/activities/tasks-and-back-stack)
* [Intents and intent filters](https://developer.android.com/guide/components/intents-filters)
* [Sending the user to another app](https://developer.android.com/training/basics/intents/sending)
* [Common intents](https://developer.android.com/guide/components/intents-common)

Video lecture (in Portuguese): (__coming soon__)

### 13/10/2023 - Practical class
* Goal: Building the UI navigation of the project
* Project: Complete the implementation of the screen for displaying information about the application's authors.
* Project: Create the application's navigation graph and implement the navigation between the screens.
* Project: Finish the implementation of the game's screen and the game logic, presuming that both players use the same device.
* Project: Create automatic tests for all the screens and for the game logic that is implemented.