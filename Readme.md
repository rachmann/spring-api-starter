## Resson API Starter Kit

## Steps to Setup for Local Development

**1. Configure PostgreSQL**

First, create a database. 
https://www.codementor.io/engineerapart/getting-started-with-postgresql-on-mac-osx-are8jcopb

Then configure `src/main/resources/application.properties` with the appropriate details from created DB


**3. Run the app**

Type the following command from the root directory of the project to run it -

```bash
mvn spring-boot:run
```

Build with VS code for hot reloading -

Alternatively, you can package the application in the form of a JAR file and then run it like so -

```bash
mvn clean package
java -jar target/api-0.0.1-SNAPSHOT.jar
```

## Steps to Setup on GCP