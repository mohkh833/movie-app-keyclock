

# 🎬 Movie Management Application

## 📌 Overview

This is a **Movie Management Application** built as part of the Associate Software Engineer role assessment. The application consists of two dashboards:

* **Admin Dashboard**: Allows admin users to search for movies via the **OMDB API**, add/remove movies to the database, perform **batch add/delete**, and manage the movie collection.
* **Regular User Dashboard**: Allows regular users to view the movie list added by the admin and see detailed information about each movie.

---

## 🚀 Features

### ✅ Mandatory Features

* **Admin Dashboard**

  * Admin login functionality
  * Search movies from **OMDB API**
  * Add selected movies to the database
  * Remove movies from the database
  * **Batch Add & Batch Delete** movies

* **Regular User Dashboard**

  * User login functionality
  * View a list of movies added by the admin
  * View movie details

### ⭐ Nice to Have

* **Search bar** for regular users to find specific movies
* **User rating system** to rate movies

### 🎯 Bonus Features

* **Pagination** for better user experience

---

## 🛡️ Authentication & Authorization

The application uses **Keycloak** for authentication and authorization.

* **Keycloak Realm:** `movie-app`
* **Clients:**

  * `movie-frontend` (Angular app, public client)
  * `movie-backend` (Spring Boot app, bearer-only)
* **Roles:**

  * `ROLE_ADMIN`: Can search OMDB, add/remove movies, and perform **batch operations**
  * `ROLE_USER`: Can view movie list and details

👉 Admin login is now handled via **Keycloak** instead of hardcoded credentials.

---

## 🛠️ Tech Stack

* **Frontend:** Angular 16+
* **Backend:** Spring Boot (Java 8+)
* **Database:** PostgreSQL
* **Authentication:** Keycloak (OAuth2 / OpenID Connect with JWT)
* **External API:** [OMDB API](https://www.omdbapi.com/)

---

## 📂 Project Structure

│── movie-frontend-app/ # Angular frontend
│── movie-backend-app/ # Spring Boot backend
│── README.md # Project documentation
│── docker-compose.yml # (Optional) Docker setup

---

## 🔧 Setup and Installation

### 🔹 Backend Setup

1. Navigate to the backend folder.
2. Configure the `application.properties` file with your database & Keycloak credentials.
3. Run `docker-compose up -d` to start **PostgreSQL** and **Keycloak**.
4. Build and run the Spring Boot application:

   ```bash
   mvn spring-boot:run
   ```

### 🔹 Frontend Setup

1. Navigate to `movie-frontend-app/`.
2. Install dependencies:

   ```bash
   npm install
   ```
3. Run the Angular development server:

   ```bash
   ng serve
   ```
4. The frontend will be available at: [http://localhost:4200](http://localhost:4200).

### 🔹 Authentication

* Keycloak runs on: [http://localhost:8080](http://localhost:8080)
* Admin and User accounts should be created in Keycloak.
* Example roles:

  * **Admin** → Full access
  * **User** → Read-only access

---

## 📌 Future Enhancements

* Implement movie recommendations based on user ratings.
* Improve UI/UX with better styling and animations.
* Add unit and integration tests for backend and frontend.

