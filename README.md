# Spring Boot Docker Postgres Application

## 1. Development Enviroment
`Window 10 pro`

`Java-8`

`Intelliji IDEA Untimate 2017.3`

## 2. Build app

`mvn clean package`

`docker build -f src/main/docker/Dockerfile -t app .`

`cd src/main/docker`

`docker-compose up`

## 3. Introduction

`To display  Document List Screen, Open Address: http://localhost:8080.`

`To upload  Document, On document list screen, choose file from local and click button upload.`

`To switch between Document List screen and Task List Screen, click button Document List or Task List on Menu area.`

## 4. Reference

`https://spring.io/blog/2015/03/08/getting-started-with-activiti-and-spring-boot`

`http://www.baeldung.com/spring-activiti`

`https://github.com/jirkapinkas/spring-boot-postgresql-docker-compose`

`https://github.com/42BV/spring-boot-docker-postgres`


