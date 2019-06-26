# About
This is Weather App. This app can provide information about current weather 
at particular location on particular day. This app is made to demonstrate MVP and
Repository design pattern. MVP pattern is used to handle user interaction and
the business logic. Repository pattern is used to handle data access from
different sources: cache, database, and active API.

## Data Sources
1. **Cache** is mocked by creating a data store class which is able to invalidate
saved data when the data has been expired.
2. **Database** is mocked by creating a class which is able read and save by
reading and writing files.
3. **Active API** is mocked by creating a data generator which is able to 
randomly give data and ignore any received data.

# Development Setup
- Kotlin compiler version **1.3.31** to build the source code.
- JDK version **12.0.1** to run the jar.

# Test
> ./gradlew clean test

# Build
To run test and build:
> ./gradlew build

# Run
> ./gradlew run -q --console=plain
