# PVP21 Group 6
This repository holds our code and external dependencies.

## Project structure
- `client` - contains the JavaFX client code
- `server` - contains the Spring Boot server code
- `shared` - common code used by both `client` and `server`
- `external` - external libs and applications

## Building
We use Gradle to manage dependencies and build the code. AdoptOpenJDK 16 (HotSpot) was used during development.

### IntelliJ
- Make sure the Gradle plugin is installed
- Clone the repository
- [Open the Gradle project](https://www.jetbrains.com/help/idea/gradle.html#gradle_import_project_start) (tasks should be visible in the Gradle tool window if loaded correctly) 
- In the Gradle window, run `Tasks -> build -> build`
- To run the application, use the `server -> Tasks -> application -> bootRun` and `client -> Tasks -> application -> run` tasks

### Command line
- Clone the repository
- `./gradlew build`
- `./gradlew server:bootRun` or `./gradlew client:run` to run the server/client

## Contributors
- Dlindbla
- crprl
- kousam
- halvnykterist
- Szanne
- theeVice
