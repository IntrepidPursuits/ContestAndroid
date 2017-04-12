# Judgy (ContestAndroid)

[![Build Status](https://ci.intrepid.io/buildStatus/icon?job=Contest-Android)](https://ci.intrepid.io/job/Contest-Android/)
[![Coverage](http://ci.intrepid.io:9913/jenkins/cobertura/Contest-Android/)](https://ci.intrepid.io/job/Contest-Android/cobertura/)

Judgy aims to facilitate the mediation of local contests so that everyone can focus on enjoying the event.

**HOW IT WORKS:**
- Contest creators can set rules about what attributes should be considered by participants.
- Contestants and judges are invited by the owner to participate via a unique code.
- After all submissions are sent by contestants and scored by judges, all contest participants get notified about results.

___
# Table of Contents

1. [Building](#building)
   1. [Onboarding](#onboarding)
   1. [Configuration](#configuration)
   1. [Running](#running)
1. [Testing](#testing)
1. [Release](#release)
   1. [Quirks](#quirks)
   1. [Known Bugs](#known-bugs)
1. [Architecture](#architecture)
   1. [Data Flow](#data-flow)
   1. [Core Technology #1](#core-technology-1)
   1. [Core Technology #2](#core-technology-2)
   1. [Third Party Libraries](#third-party-libraries)
1. [History](#history)

___

# Building
## Onboarding
Install dependencies by building the project. It doesn't require any other special configuration to run.

## Configuration
There are three different types of builds: `debug`, `qa`, and `release`.
For differences, check out the gradle configuration. Some highlights are:
- API: `debug` has the flag `DEV_BUILD` and uses a mock API, while `qa` and `release` use the real API endpoint
- Logs: are enabled for `debug` and `qa`
- Release: the build type `release` has never been configured

## Running
Run the app in either the emulator or on a device.

[See Quirks](#quirks).
___

# Testing

- Unit tests exist under the "test" directory. We are aiming to each 100% code coverage on Presenters.
- You can run the code coverage locally by running `./gradlew unitTestCoverage` from the command line.
- There are no UI (Espresso) tests at this moment. The "androidTest" directory holds the Skeleton code only.

# Release
There have been no releases of this app.

## Quirks
### User ID
Judgy has no registration or login: the user is created through the API when the app is opened, and saved in the user preferences until it is cleared.

At this moment there is no way to recover your user ID and content if the preferences are cleared.

To make manual testing easier, shaking your phone will clear the preferences and you will be able to test the app as a new user.
Note that to our knowledge this is not possible in the emulator.

### Choosing API
The `debug` buildtype uses a mock API (see [Configuration](#configuration)).
If you want to test the `debug` build with the real API, use `RetrofitClient.getApi()` in class `ContestApplication`.

## Known Bugs
Bugs are reported in Jira.
___

# Architecture
We use MVP.

## Data Flow
## Core Technology 1
## Core Technology 2
## Third Party Libraries
- RxJava/RxAndroid
- Retrofit
- Gson
- OkHttp
- Butterknife
- Calligraphy
- Picasso
- UCrop for cropping images
- Mockito
- Timber
- Intrepid common utils
___

## Info
### Slack channels
- judgy (both to talk and for jenkins reports)
___


# History
Started as an apprentice project in Winter 2017 in parallel with [Judgy iOS](https://github.com/IntrepidPursuits/contest-app-ios/).
