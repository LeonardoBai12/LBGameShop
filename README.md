# LB GameShop

LB GameShop is a game store simulator, consuming the [CheapShark API](https://apidocs.cheapshark.com) .

## Description

LB GameShop offers the following features:
* Create new game orders
* Search for cheap games on most of the stores

## Technologies

The application is built using the following technologies:

* [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for asynchronous programming.
* [ViewModel](https://developer.android.com/reference/androidx/lifecycle/ViewModel) for managing UI-related data.
* [Retrofit](https://square.github.io/retrofit) for consuming RESTful web services.
* [Jetpack Compose](https://developer.android.com/jetpack/compose/documentation) for building the UI.
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection.
* [Firebase Realtime Database](https://firebase.google.com/docs/database?hl=pt-br) for Cloud storage.
* [Firebase Authentication](https://firebase.google.com/docs/auth?hl=pt-br) for user authentication.
* [JUnit 5](https://junit.org/junit5/docs/current/user-guide) for unit testing.
* [MockK](https://mockk.io) for mocking objects in unit tests.
* [Jetpack Compose UI Testing](https://developer.android.com/jetpack/compose/testing) for UI testing.
* [Github Actions](https://docs.github.com/pt/actions/learn-github-actions) for Continuous Integration/Continuous Deployment (CI/CD)
* [Dokka](https://github.com/Kotlin/dokka) for generating documentation.
* [JaCoCo](https://www.jacoco.org) for generating test coverage reports.

## Getting Started

1. Download the LBGameShop.apk file from the [Build and Deploy APK](https://github.com/LeonardoBai12/LBGameShop/actions/workflows/build_and_deploy_workflow.yml) action artifacts.
2. Install the APK on your Android device.
3. Use the application to make some game orders. =)

## Quality Assurance

To ensure high-quality code, the following tools and processes are used before merging any pull requests:

* [Ktint](https://pinterest.github.io/ktlint/) is used to enforce code style guidelines.
* All unit tests are run to ensure code functionality and quality.

This process helps maintain code consistency and quality throughout the project.

## Documentation

The documentation is automatically generated and published for every push to the main branch.\
To access the documentation, download the _lb-gameshop-documentation_ file from the [Documentation](https://github.com/LeonardoBai12/LBGameShop/actions/workflows/documentation_workflow.yml) action artifacts.

## Coverage Report

An unit test coverage report is generated and published for every push to the main branch.\
To access the test coverage report, download the _lb-gameshop-coverage-report_ file from the [Coverage Report](https://github.com/LeonardoBai12/LBGameShop/actions/workflows/coverage_report_worflow.yml) action artifacts.
