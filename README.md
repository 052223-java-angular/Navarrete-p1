# P1 - Movie Application

## Introduction

This is a full stack movie application that will use Angular on the frontend and Springboot with a Postgres database on the backend. An external api called TMBD will also be used to retrieve movie data.

## User Stories

- **As a user**, I want to register an account so that I can have a personalized experience
- **As a user**, I want to log in to my account so that I can access my movie lists.
- **As a user**, I want to browse through movies only when logged in.
- **As a user**, I want to search for movies by name, category, or rating range so that I can find the movie I am looking for.
- **As a user**, I want to be able to create movie lists so I can group together my movies.
- **As a user**, I want to add movies to my movie lists so I can keep track of movies or group them together by a certain topic.
- **As a user**, I want to modify the movies in my movie lists by removing them.
- **As a user**, I want to modify my movies lists by deleting them.
- **As a user**, I want to rate and review movies so that I can share my experience with other users.
- **As a user**, I want to view ratings and reviews from other users so that I can make a decision on which movies to watch.


## MVP (Minimum Viable Product)

- User registration and login
- User roles for authentication
- Browsing and searching for movies
- Search for movies using a filter
- Adding movies to movie lists
- Modifying the movie lists and movies in a movie list
- movie rating and reviewing

## Stretch Goals

- Implementing a recommendation system based on user's previous watched movies or favorite movies
- Implementing a streaming service recomendation system that give users the cheapest services to watch a list of movies
- Implementing a community polling system

## Tech Stacks

- **Java**: The backend programming language used for building the application.
- **Typescript**: A strongly typed programming language that is a superset of Javascript used in the Angular framework.
- **Tailwind**: A CSS framwork used to style the the client side application.
- **PostgreSQL**: Used as the database to store user, movie lists, and bookmarks.
- **Springboot**: A backend framework used to build application's REST API.
- **Angular**: Frontend framework used to build the client side application.
- **Lombok**: To reduce boilerplate code;
- **Maven or Gradle**: Used for managing project dependencies.
- **JUnit**: A testing framework for Java applications, used to ensure our code works as expected.
- **JDBC (Java Database Connectivity)**: An API for connecting and executing queries on the database.
- **BCrypt**: A Java library for hashing and checking passwords for security.
- **JUnit, Mockito, and PowerMock**: Used for unit and integration testing.
- **Git and GitHub**: Used for version control.

## Requirements

- **Clean Codebase**: All code should be clean and well-documented. The repository should not include any unnecessary files or folders such as the `target/`, `.DS_Store`, etc. All files and directories should be appropriately named and organized.

- **Database Design**: The database should be designed following the principles of the 3rd Normal Form (3NF) to ensure data integrity and efficiency. An Entity Relationship Diagram (ERD) should be included in the documentation.

- **Secure**: All sensitive user data such as passwords must be securely hashed before storing it in the database. The application should not display any sensitive information in error messages.

- **Error Handling**: The application should handle potential errors gracefully and provide clear and helpful error messages to the users.

- **Testing**: The application should have a high test coverage. Unit tests and integration tests should be implemented using JUnit, Mockito, and PowerMock.

- **Version Control**: The application should be developed using a version control system, preferably Git, with regular commits denoting progress.

- **Documentation**: The repository should include a README file with clear instructions on how to run the application. Code should be well-commented to allow for easy understanding and maintenance.

- **Scalable**: The design of the application should be scalable, allowing for easy addition of new features or modifications in the future.


# ERD

![entity relationship diagram](src/main/resources/db/p1-erd.png?raw=true)