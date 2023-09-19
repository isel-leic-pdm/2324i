
## Week 1
### 11/09/2023 - Course introduction

* Syllabus, teaching methodology and bibliography.
  * Evaluation
  * Resources

For reference:
* [Download Android Studio & App Tools](https://developer.android.com/studio)
* [Android API Levels](https://apilevels.com/)
* [SDK Platform release notes | Android Developers](https://developer.android.com/studio/releases/platforms)

Video lecture (in Portuguese): 
* [Android @ ISEL - 2023 - (01) Apresentação](https://www.youtube.com/watch?v=S6bl3bHU7ZU&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=1)

### 15/09/2023 - Practical class
* Goal: Install Android Studio and create a new project (the *Hello Android* app) using Andrdoid Studio's wizard.
* Challenge: From the miriad of questions that may arise, elect the two most relevant ones for refining your mental model of the Android platform. The professor will use them as talking points in the next class.

## Week 2
### 18/09/2023 - Building Android UIs with Jetpack Compose (part 1)
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
* Automated tests in Android Studio (introduction)
  * Instrumented tests: testing a Compose UI

For reference:
* [Thinking in Compose](https://developer.android.com/jetpack/compose/mental-model)
* [Compose layout basics](https://developer.android.com/jetpack/compose/layouts/basics)
* [State in Jetpack Compose](https://developer.android.com/jetpack/compose/state)
* [Testing Jetpack Compose UIs](https://developer.android.com/jetpack/compose/testing)

Video lecture:
* Android @ ISEL - 2023 - (02) Jetpack Compose (parte 1) _(coming soon)_

### 22/09/2023 - Practical class
* Goal: Exercise building a UI with Jetpack Compose
* Challenge 1: Build an application for counting persons entering and leaving a room. The application is comprised of a single screen that displays the current number of persons in the room and has two buttons, one for incrementing the count and another for decrementing it.
* Project: Build the screen for displaying information about the application's authors. For now, focus on the screen's layout. No behaviour is required at this time.
