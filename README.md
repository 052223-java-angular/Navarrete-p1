# P1 - Movie Application

## Introduction

This is a full stack movie application that will use Angular on the frontend and Springboot with a Postgres database on the backend. An external api called TMBD will also be used to retrieve movie data.

## User Stories

- **As a user**, I want to register an account so that I can have a personalized experience
- **As a user**, I want to log in to my account so that I can access my movie/show lists.
- **As a user**, I want to browse through movies/shows only when logged in.
- **As a user**, I want to search for movies/shows by name, category, or IMDb rating so that I can find the movie/show I am looking for.
- **As a user**, I want to add movies/shows to my movie/show lists or bookmark movies/shows to plan on watching them later.
- **As a user**, I want to modify the movies/shows in my movie/show lists by removing them or changing bookmark category.
- **As a user**, I want to rate and review movies/shows so that I can share my experience with other users.
- **As a user**, I want to view ratings and reviews from other users so that I can make a decision on which movies/shows to watch.


## MVP (Minimum Viable Product)

- User registration and login
- Browsing and searching for movies/shows
- Adding movies/shows to movie/show lists or adding a bookmark category
- Modifying the movie/show list and bookmarks
- Movie/show rating and reviewing

## Stretch Goals

- Implementing a recommendation system based on user's previous watched movies/shows or favorite movies/shows
- Implementing a streaming service recomendation system that give users the cheapest services to watch a list of movies/shows
- Implementing a community polling system

## Tech Stacks

- **Java**: The backend programming language used for building the application.
- **Typescript**: A strongly typed programming language that is a superset of Javascript used in the Angular framework.
- **Tailwind**: A CSS framwork used to style the the client side application.
- **PostgreSQL**: Used as the database to store user, movie/show lists, and bookmarks.
- **Springboot**: A backend framework used to build application's REST API.
- **Angular**: Frontend framework used to build the client side application.
- **Maven or Gradle**: Used for managing project dependencies.
- **JUnit**: A testing framework for Java applications, used to ensure our code works as expected.
- **Log4j**: A logging utility for debugging purposes.
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

