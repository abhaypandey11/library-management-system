# 📚 Library Management System

A full-stack Library Management System built with
Spring Boot, Hibernate, and PostgreSQL.

## 🛠️ Tech Stack
- **Backend:** Java, Spring Boot, Spring Data JPA, Hibernate
- **Database:** PostgreSQL
- **Frontend:** HTML, CSS, JavaScript
- **Tools:** Maven, Lombok, ModelMapper

## 🏗️ Architecture
- Controller Layer — REST endpoints
- Service Layer — Business logic
- DAO Layer — Database operations
- Repository Layer — JPA queries
- Model Layer — Entity classes
- DTO Layer — Request/Response objects
- Exception Layer — Global exception handling

## ✨ Features
- Add, Update, Delete, Search Books
- Register and Manage Members
- Issue Books to Members
- Return Books with Automatic Fine Calculation (₹5/day)
- Dashboard with live statistics
- Input validation and error handling

## 🚀 How to Run
1. Clone the repository
2. Create PostgreSQL database named librarydb
3. Copy application-example.properties to application.properties
4. Fill in your database credentials
5. Run LibraryManagementApplication.java
6. Open http://localhost:8080/index.html

## 📡 API Endpoints
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/books | Get all books |
| POST | /api/books | Add new book |
| PUT | /api/books/{id} | Update book |
| DELETE | /api/books/{id} | Delete book |
| GET | /api/members | Get all members |
| POST | /api/members | Register member |
| PUT | /api/members/{id} | Update member |
| DELETE | /api/members/{id} | Delete member |
| POST | /api/issues | Issue book |
| PUT | /api/issues/return/{id} | Return book |
| GET | /api/issues | Get all issues |
| GET | /api/issues/member/{id} | Get issues by member |