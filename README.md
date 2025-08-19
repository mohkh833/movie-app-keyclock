# Movie Management Application  

## 📌 Overview  
This is a **Movie Management Application** built as part of the Associate Software Engineer role assessment. The application consists of two dashboards:  

- **Admin Dashboard**: Allows admin users to search for movies via the **OMDB API**, add/remove movies to the database, and manage the movie collection.  
- **Regular User Dashboard**: Allows regular users to view the movie list added by the admin and see detailed information about each movie.  

## 🚀 Features  

### ✅ Mandatory Features  
- **Admin Dashboard**  
  - Admin login functionality  
  - Search movies from **OMDB API**  
  - Add selected movies to the database  
  - Remove movies from the database  

- **Regular User Dashboard**  
  - User login functionality  
  - View a list of movies added by the admin  
  - View movie details  

### ⭐ Nice to Have  
- **Search bar** for regular users to find specific movies  
- **User rating system** to rate movies  

### 🎯 Bonus Features  
- **Pagination** for better user experience  

## 🛠️ Tech Stack  
- **Frontend:** Angular 16+  
- **Backend:** Spring Boot (Java 8+)  
- **Database:** PostgreSql  
- **Authentication:** JWT-based authentication (or session-based login)  
- **External API:** [OMDB API](https://www.omdbapi.com/)  

## 📂 Project Structure  
│── movie-frontend-app/ # Angular frontend
│── README.md # Project documentation
│── docker-compose.yml # (Optional) Docker setup

## 🔧 Setup and Installation  

### 🔹 Backend Setup  
1. Navigate to the root folder.  
2. Configure the `application.properties` file with your database credentials.
3. first run docker file
4. Build and run the Spring Boot application: 

### 🔹 Frontend Setup  
1.npm install  
2.Install dependencies: npm install  
3.Run the Angular development server:ng serve  
4.The frontend will be available at: http://localhost:4200/.

### 🔹 Authentication
Admin: admin@example.com/ Admin@1234

📌 Future Enhancements

    Implement movie recommendations based on user ratings.
    Improve UI/UX with better styling and animations.
    Add unit and integration tests for backend and frontend.

Submission

This project was submitted as part of the Associate Software Engineer hiring process at Fawry.


 




