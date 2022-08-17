# User Management System

#### Overview
>User management microservice in Spring Boot using MySql and can be run in its own Docker containers

Docker with two different containers:
* one Spring Boot REST Apis
* one MySql 8.0 database

#### Exposed REST apis
Here below the most relevant features exposed using REST Apis:

#### User management features

* Login with username & password
* Basic authentication to enforce access control
* Retrieve the list of all the existing user accounts
* search and filter users based on any criteria
* Register a new user account
* Update user account data 
* Add or remove a role on an user account
* Delete a user account
* Standard validation for email, phone, password

## REST apis exposed
REST apis with Swagger and Ope Api docs:

http://localhost:8081/swagger-ui.html

http://localhost:8081/v3/api-docs

Postman can also be used.

## Quick Start

### Setup using Docker containers
>Using Docker it’s easy to create scalable and manageable applications built of microservices.

The project is designed to use two containers:
* one Java microservice
* one MySql local database

> CREATE DATABASE IF NOT EXISTS users;

Execute the run bash script to compile and run the microservice container:
    ./run.cmd

Open a browser and explore the REST apis:

http://localhost:8081/swagger-ui.html

The project should be up and running.

Everything should be up and running :)


### Setup without Docker
>You can also setup and work on this project without to consider to use Docker.
You will just launch the Spring Boot application targeting the MySql database (on localhost or on a remote one).

Install Java 8 JDK.

Set up your MySql instance and create the empty database "users":

    CREATE DATABASE IF NOT EXISTS users;

Create and grant a new MySql user on the "users" database.

Open the application.properties file located in /src/main/resources.

Target your localhost MySql database:

    spring.datasource.url=jdbc:mysql://localhost:3306/users

Set the username and password of the MySql's user:

    spring.datasource.username=root
    spring.datasource.password=password

Execute the microservice code using Maven:

    ./mvnw spring-boot:run
    
Open a browser and explore the REST apis:

http://localhost:8081/swagger-ui.html

The service should be up and running.

Everything should be up and running using your local MySql :)
