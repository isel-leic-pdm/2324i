
## Week 1
### 11/09/2023 - Course introduction

* Syllabus, teaching methodology and bibliography.
  * Evaluation
  * Resources

For reference:
* [Download Android Studio & App Tools](https://developer.android.com/studio)
* [Android API Levels](https://apilevels.com/)
* [SDK Platform release notes | Android Developers](https://developer.android.com/studio/releases/platforms)

Video lecture (in Portuguese): (recorded in LEIC51D class) 
* [Android @ ISEL - 2023 - (01) Apresentação](https://www.youtube.com/watch?v=S6bl3bHU7ZU&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=1)

### 14/09/2023 - Practical class
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

Video lecture (in Portuguese): (recorded in LEIC51D class) 
* [Android @ ISEL - 2023 - (02) Jetpack Compose (parte 1)](https://www.youtube.com/watch?v=jjug-eYRlZQ&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=2)

### 21/09/2023 - Practical class
* Goal: Exercise building a UI with Jetpack Compose
* Challenge 1: Build an application for counting persons entering and leaving a room. The application is comprised of a single screen that displays the current number of persons in the room and has two buttons, one for incrementing the count and another for decrementing it.
* Project: Build the screen for displaying information about the application's authors. For now, focus on the screen's layout. No behaviour is required at this time.

## Week 3
### 25/09/2023 - Building Android UIs with Jetpack Compose (part 2)
* Activity component, continued: 
  * Lifecycle: `onCreate`, `onStart`, `onStop` and `onDestroy`
* Jetpack Compose, continued
  * State management: state hoisting
  * Execution in compose (concurrency model): 
    * In @Composable functions
    * In event handlers
  * Effects in @Composable functions
    * `LaunchedEffect` - for launching coroutines as a consequence of a composition
    * `rememberCoroutineScope` - for obtaining a scope to launch coroutines in event handlers
* Automated tests in Android Studio, continued
  * Unit tests: testing the domain logic
  * Instrumented tests: testing a Compose UI using test doubles

For reference:
* [Introduction to the Activity component](https://developer.android.com/guide/components/activities/intro-activities)
* [State and Jetpack Compose](https://developer.android.com/jetpack/compose/state)
* [Side-effects in Compose](https://developer.android.com/jetpack/compose/side-effects)
* [Kotlin coroutines on Android](https://developer.android.com/kotlin/coroutines)
* [Test Apps on Android](https://developer.android.com/training/testing)
  * [Test doubles in Android](https://developer.android.com/training/testing/fundamentals/test-doubles)
  * [Mockk, a mocking library for Kotlin](https://mockk.io/)
* [Challenge 1 solution](https://github.com/isel-leic-pdm/2324i/tree/main/challenges)

Video lecture (in Portuguese): (recorded in LEIC51D class)
* [Android @ ISEL - 2023 - (03) Jetpack Compose (parte 2)](https://www.youtube.com/watch?v=A31AXou8_dc&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=3)

### 28/09/2023 - Practical class
* Goal: Exercise building a UI with Jetpack Compose using state hoisting and effects
* Challenge 2: Build a StopWatch application, for counting time. The application has a screen with the current count and buttons to start, stop and restart the count.
* Project: Build the screen for playing the game (with partial implementation of the behavior, that is, moves are made but the rules of the game are not yet applied). Create automatic tests for the screen and for the game logic that is implemented. There's still no navigation between the screens.

## Week 4
### 02/10/2023 - Building Android UIs with Jetpack Compose (wraping up)
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

Video lecture (in Portuguese): (recorded in LEIC51D class)
* [Android @ ISEL - 2023 - (04) Jetpack Compose (considerações de desenho)](https://www.youtube.com/watch?v=4C2v3Bv54WM&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=4)


### 05/10/2023 - National holiday (no classes)

## Week 5
### 09/10/2023 - Android UX: navigating between screens
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

Video lecture (in Portuguese): (recorded in LEIC51D class)
* [Android @ ISEL - 2023 - (05) Navegação](https://www.youtube.com/watch?v=DPd4hKkRRO8&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=5)

### 12/10/2023 - Practical class
* Goal: Building the UI navigation of the project
* Project: Complete the implementation of the screen for displaying information about the application's authors.
* Project: Create the application's navigation graph and implement the navigation between the screens.
* Project: Finish the implementation of the game's screen and the game logic, presuming that both players use the same device.
* Project: Create automatic tests for all the screens and for the game logic that is implemented.

## Week 6
### 16/10/2023 - State management in Android (part 1)
* Activity component, revisited: 
  * Lifecycle: behaviour on reconfiguration
* State management in Android
  * Presentation state vs application state
  * Application state, characterized:
    * Local to the screen
    * Global to the application (i.e. shared across screens) 
* View model
  * Purpose and motivation
  * Lifecycle
  * As a means to preserve local application state across configuration changes
  * As the host of the execution of the screen's domain logic

For reference:
* [ViewModel overview](https://developer.android.com/topic/libraries/architecture/viewmodel)
  * [ViewModel lifecycle](https://developer.android.com/topic/libraries/architecture/viewmodel#lifecycle)
  * [Use Kotlin coroutines with lifecycle-aware components](https://developer.android.com/topic/libraries/architecture/coroutines#viewmodelscope)
* [Testing Kotlin coroutines on Android](https://developer.android.com/kotlin/coroutines/test) 

Video lecture (in Portuguese): (recorded in LEIC51D class)
* [Android @ ISEL - 2023 - (06) Gestão de estado (parte 1)](https://www.youtube.com/watch?v=0TQmDxItbqQ&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=6)

### 19/10/2023 - Practical class
* Goal: Make the project's screens resilient to configuration changes 
* Project: Add view models to the project's screens that require resilience to configuration changes. Do not forget the automatic tests.

## Week 7
### 23/10/2023 - HTTP communication
* The Android device as an HTTP client
  * Motivation and consequences of distribution
  * Required permissions
* HTTP comunication with OkHttp
  * Programming model
  * Making asynchronous requests
  * Bridging between OkHttp's and Kotlin's concurrency models
* JSON serialization and desserialization with Gson
* Android Application class
  * Motivation and lifecycle
  * Using Application for manual dependency injection

For reference:
* [Permissions overview](https://developer.android.com/guide/topics/permissions/overview)
* [Permissions for network access](https://developer.android.com/training/basics/network-ops/connecting)
* [OkHttp](https://square.github.io/okhttp/)
  * [Making asynchronous requests](https://square.github.io/okhttp/recipes/#asynchronous-get-kt-java)
* [Gson](https://github.com/google/gson#gson)
* [Application class](https://developer.android.com/reference/android/app/Application)
  * [Manual dependency injection with Application](https://developer.android.com/training/dependency-injection/manual#basics-manual-di)

Other links: 
* [Ngrok](https://ngrok.com/)

Video lecture (in Portuguese): (recorded in LEIC51D class)
* [Android @ ISEL - 2023 - (07) Comunicação HTTP](https://www.youtube.com/watch?v=LwO_o0flPB4&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=7)

### 26/10/2023 - Practical class
* Goal: Implement the communication with the Web API (for Option C)
* Project: (Option C) Implement the aspects related to HTTP communication on the game screen. This includes creating the DTOs and implementing the service used for interaction with the API.
* Project: (Option B) Consolidate the implementation of the application's screen's and the game logic presuming that both players use the same device. Create automatic tests for all the screens and for the game logic that is implemented.

## Week 8
### 30/10/2023 - State management in Android (part 2)
* Navigation between activities, revisited
  * Sending data between activities using Intent extras
    * Parcelable contract
    * Parcelable implementation generator and the @Parcelize annotation
* Application state vs presentation state, revisited
  * Presentation state:
    * Preserving it outside the hosting process: Parcelable contract, again
    * Saving and restoring state in Jetpack Compose: rememberSaveable

For reference:
* [Lists and grids in Jetpack Compose](https://developer.android.com/jetpack/compose/lists)
* [Sending data between activities](https://developer.android.com/guide/components/activities/parcelables-and-bundles#sdba)
  * [Parcelable](https://developer.android.com/reference/android/os/Parcelable)
  * [Parcelable implementation generator](https://developer.android.com/kotlin/parcelize)
* [Saving UI state](https://developer.android.com/topic/libraries/architecture/saving-states)

Video lecture (in Portuguese): (recorded in LEIC51D class)
* [Android @ ISEL - 2023 - (08) Gestão de estado (parte 2)](https://www.youtube.com/watch?v=GdqCaKdYKzY&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=8)

### 02/11/2023 - Practical class
* Goal: Implement the project's screens containing lists
* Project: (Option C) Implement the screen for displaying the global ranking. This includes the implementation of the service used for interaction with the API. Do not forget the automatic tests.
* Project: (Option B) Implement the screen for displaying the list of favorite games. Use a fake implementation of the service used for interaction with Firestore. Do not forget the automatic tests.

## Week 9
### 06/11/2023 - Considerations on software design in Android: coding session
* Building the Tic-Tac-Toe application
  * Specifying the application's User eXperience (UX)
    * Identifying the application's screens and their purpose
    * Specifying the application's navigation graph
  * Identification of the main elements of the solution
    * Domain data (immutable)
    * Domain operations (pure functions)
    * Presentation state (hosted on the view model)
    * Application state
      * Local to the screen (hosted on the view model)
      * Global to the application (hosted on the Application class)
* Building the application, tests first
  * Automated tests as a means of specifying the application's behaviour

Video lecture (in Portuguese): (recorded in LEIC51D class)
* [Android @ ISEL - 2023 - (09) Sessão de codificação: Tic Tac Toe](https://www.youtube.com/watch?v=nedq2Rt2aZI&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=9)

### 09/11/2023 - Practical class
* Goal: Implement the project's screens that collect data from the user and store it in the application's state
* Project: (Option C) To implement the screen for collecting user credentials (username and password) using a fake repository implementation that will contain the user token resulting from the login procedure. Modify the remaining screens to make use of this repository. Next week we will replace the fake implementation with a real one, using the DataStore API. Do not forget the automatic tests.
* Project: (Option B) To implement the screen for collecting user information (nickname and motto) using a fake repository implementation that contains this information. Modify the remaining screens to make use of this repository. Next week we will replace the fake implementation with a real one, using the DataStore API.

## Week 10
### 13/11/2023 - State management in Android (part 3)
* Building the application, tests first (continued)
  * Automated tests as a means of specifying the application's behaviour
* Android concurrency model, continued: Kotlin flows
  * Purpose and motivation (comparison with Kotlin Sequences)
  * Transformations, e.g. `map`, `filter`
  * Terminal operations, e.g. `collect`, `first`, `last`
* Persistent storage of application state
  * For simple data (schemaless) and with low volume, using DataStore
  * The DataStore API: Preferences DataStore

Video lecture (in Portuguese): (recorded in LEIC51D class)
* [Android @ ISEL - 2023 - (10a) Sessão de codificação: Tic Tac Toe](https://www.youtube.com/watch?v=jkGwXMTHurY&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=10)
* [Android @ ISEL - 2023 - (10b) Gestão de estado (parte 3)](https://www.youtube.com/watch?v=K123jevBEQc&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=11)

For reference:
* [Kotlin Flows](https://kotlinlang.org/docs/flow.html)
* [Kotlin Flows on Android](https://developer.android.com/kotlin/flow#jetpack)
* [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

### 16/11/2023 - Practical class
* Goal: Implement the project's screens that collect data from the user and store it in the application's state
* Project: (Option C) Implement the repository that will contain the user token resulting from the login procedure using the DataStore API. Do not forget the automatic tests.
* Project: (Option B) Implement the repository that will contain the user information (nickname and motto) using the DataStore API. Do not forget the automatic tests.

## Week 11
### 20/11/2023 - Data flows in Android (part 1)
* Interaction models (a.k.a. data flow models)
  * Pull model: the data is pulled from the destination to the source (receptor's initiative)
  * Push model: the data is pushed from the source to the destination (emitter's initiative)
* Data flows in Android using the push model
  * Between the `Activity` and its `ViewModel`
    * `StateFlow` and `MutableStateFlow` - motivation and applicability
  * Between the `Activity` and the `@Composable` function that implements its screen
    * Extension function `collectAsState` - motivation and applicability

Video lecture (in Portuguese): (recorded in LEIC51D class)
* [Android @ ISEL - 2023 - (11) Fluxos de dados em Android (parte 1)](https://www.youtube.com/watch?v=Yk2rwi7YIBY&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=12)

For reference:
* [StateFlow and SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)
* [State and Jetpack Composes](https://developer.android.com/jetpack/compose/state#use-other-types-of-state-in-jetpack-compose)

### 23/11/2023 - Practical class
* Goal: Increase the robustness of our application, making sure it is resilient to configuration changes
* Project: Finish the implementation of pending functionalities, making sure that they are resilient to configuration changes. Optionally, redesign the solution architecture to use a push interaction model, using Kotlin flows. The redesign should be incremental, one screen at a time. Don't forget the automated tests.

## Week 12
### 27/11/2023 - Data flows in Android (part 2)
* Data flows in Android using the push model (continued)
  * Between the `Activity` and its `ViewModel`, revisited
  * Between the `Activity` and the `@Composable` revisited
  * Between the domain and the `ViewModel`
* Discussion of the resulting architecture and its properties
* Persistency using Firebase's Firestore:
  * Overview of the provided data model (i.e. Document DB)
    * Documents, collections and references
    * Data types
  * Android SDK API overview:
    * Adding, updating and deleting data
    * Reading data: Queries and observable queries

Video lecture (in Portuguese): (recorded in LEIC51D class)
* [Android @ ISEL - 2023 - (12) Fluxos de dados em Android (parte 2)](https://www.youtube.com/watch?v=B8I71UfK-_k&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=13)

For reference:
* [Add Firebase to your Android project](https://firebase.google.com/docs/android/setup?hl=en&authuser=0)
* [Connect your app and start prototyping | Firebase Local Emulator Suite](https://firebase.google.com/docs/emulator-suite/connect_and_prototype?database=Firestore)
* [Cloud Firestore Data model](https://firebase.google.com/docs/firestore/data-model)
* [Supported data types | Firestore](https://firebase.google.com/docs/firestore/manage-data/data-types)
* [Add data to Cloud Firestore](https://firebase.google.com/docs/firestore/manage-data/add-data)
* [Delete data from Cloud Firestore](https://firebase.google.com/docs/firestore/manage-data/delete-data)
* [Get data with Cloud Firestore](https://firebase.google.com/docs/firestore/query-data/get-data)
* [Get real time updates with Cloud Firestore](https://firebase.google.com/docs/firestore/query-data/listen)
* [Install, configure and integrate Local Emulator Suite](https://firebase.google.com/docs/emulator-suite/install_and_configure) 

### 30/11/2023 - Practical class
* Goal: Increase the robustness of our application
* Project: Finish the implementation of pending functionalities, making sure that they are resilient to configuration changes. Optionally, redesign the solution architecture to use a push interaction model, using Kotlin flows. The redesign should be incremental, one screen at a time. Don't forget the automated tests.
* Project: (Option B) Implement the lobby using Firestore. Do not forget the automated tests.

## Week 13
### 04/12/2023 -  Execution on Android
* User-facing and non-user-facing work
* Automatic resource management in Android and its consequences on the programming model
* Scheduling non-user-facing work: WorkManager API
  * Motivation and use cases
  * General description
* Flows in Android, continued
  * Creating flows from asynchronous APIs based on callbacks: `callbackFlow`
* Android concurrency model, revisited
  * Controlling the execution of operations on flows: `withContext` and `flowOn`
* Navigation, user tasks, and back stack, revisited
  * Reacting to changes in the Activity lifecycle: `repeatOnLifecycle`

Video lecture (in Portuguese): (recorded in LEIC51D class)
* [Android @ ISEL - 2023 - (13) Execução em Android](https://www.youtube.com/watch?v=JHcrsgM6F2g&list=PL8XxoCaL3dBjc54gcE_CGnUPxwzSTAXyR&index=15)

For reference:
* [Processes and app lifecycle](https://developer.android.com/guide/components/activities/process-lifecycle)
* [Schedule Task with WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
* [Kotlin flows on Android](https://developer.android.com/kotlin/flow#callback) 

### 07/12/2023 - Practical class
* Goal: Finishing thouches on the course project

## Week 14
### 11/12/2023 - Final considerations
* Presentation of the final version of the TicTacToe application
  * Description of its architecture and key design decisions
  * Considerations about the development method used (i.e., tests first)
  * Critical analysis of the final outcome
    * Areas for improvement to make the application production-ready
* Brief historical perspective on the evolution of the Android programming model
  * General description of Android programming model elements that were not used in the course and their role in modern applications
  * Android Components: Services, BroadcastReceivers, and ContentProviders
  * System services, e.g. ConnectivityManager, AlarmManager, NotificationManager, LocationManager, etc.

Video lecture (in Portuguese): __(coming soon)__

For reference:
* [Android Components](https://developer.android.com/guide/components/fundamentals)

For future reference:
* [Services overview](https://developer.android.com/guide/components/services)
* [Broadcasts overview](https://developer.android.com/guide/components/broadcasts)
* [Content providers](https://developer.android.com/guide/topics/providers/content-providers)
* [Schedule alarms](https://developer.android.com/training/scheduling/alarms)
* [Create a notification](https://developer.android.com/develop/ui/views/notifications/build-notification)
* [Build location-aware apps | Sensors and location](https://developer.android.com/develop/sensors-and-location/location)

### 15/12/2023 - Practical class
* Goal: Finishing thouches on the course's project

## Week 15
### 18/12/2023 - Practical class
* Goal: Finishing thouches on the course's project
