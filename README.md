## Profile REST API

### Author - Roman Ledenev

**____________________________________**

## Technologies used

* [Kotlin](https://kotlinlang.org/) v1.9.25
* [Java](https://www.java.com/) v21
* [Spring Boot](https://spring.io/projects/spring-boot) v3.3.5
* [PostgreSQL](https://www.postgresql.org/) v17.2
* [Swagger](https://swagger.io/) v2.3.0
* [Liquibase](https://www.red-gate.com/products/flyway/community/) v4.27.0
* [Docker](https://www.docker.com/) v4.33.1
* [Maven](https://maven.apache.org/) v3.9.6

**____________________________________**

## Tests
**Integration tests are written with a separate test database using**

* [H2](https://www.h2database.com/html/main.html)
* [Junit 5](https://junit.org/junit5/)
* [Mockito](https://site.mockito.org/)

**____________________________________**

## How to start?

**to run locally start [Docker Desktop](https://www.docker.com/products/docker-desktop) 
and use the `run.sh` and `stop.sh` scripts** 

**or use the command from the directory with the docker-compose.yml file.**

```shell
docker-compose up --build -d
```

### Ports for start

* 8080 - profile REST API
* 5432 - postgresql relational database

***
**to run in an IDE you need to create a database in postgresql and fill in the following environment variables:**

* `SPRING_DATASOURCE_URL`
* `SPRING_DATASOURCE_USER`
* `SPRING_DATASOURCE_PASSWORD`

**____________________________________**

## How to use?

**[Swagger API Documentation](http://localhost:8080/swagger-ui/index.html) follow this link, and you will see a list of 
available REST endpoints and a description of the Data Transfer Object classes**
