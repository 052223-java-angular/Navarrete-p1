# P0 - Pair Programming eCommerce Project

## Introduction

This is a Java-based command-line interface (CLI) eCommerce application. The application will be primarily built using Java and will utilize a PostgreSQL database to store product and user information.

## User Stories

- **As a user**, I want to register an account so that I can have a personalized shopping experience.
- **As a user**, I want to log in to my account so that I can access my shopping cart and order history.
- **As a user**, I want to browse through products only when logging in.
- **As a user**, I want to search for products by name, category, or price range so that I can find what I'm looking for.
- **As a user**, I want to add products to my shopping cart so that I can purchase them later.
- **As a user**, I want to modify the quantity or remove items from my cart so that I can make changes before finalizing the purchase.
- **As a user**, I want to check out and pay for my order securely so that my personal and financial information is safe.
- **As a user**, I want to review my order history so that I can keep track of my purchases.
- **As a user**, I want to rate and review products so that I can share my experience with other users.
- **As a user**, I want to view ratings and reviews from other users so that I can make informed buying decisions.


## MVP (Minimum Viable Product)

- User registration and login
- Browsing and searching for products
- Adding products to a shopping cart
- Modifying the shopping cart
- Secure payment process
- Order history
- Product rating and reviewing

## Stretch Goals

- Implementing a recommendation system based on user's previous purchases
- Adding an admin role that can add, remove, or modify products
- Implementing promotional codes and discounts
- Adding a wish list feature

## Tech Stacks

- **Java**: The main programming language used for building the application.
- **PostgreSQL**: Used as the database to store user, product, and order data.
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

