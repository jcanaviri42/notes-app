# 📒 Notes App REST API

## 👉 Introduction
This is a project made for a challenge at Digital Harbor Bolivia.
This is a RESTful API built with Spring Boot for managing notes and tags for users. It provides endpoints for creating, retrieving, updating, and deleting notes, as well as associating tags with notes and advanced search capabilities.

Technologies used (Spring Boot, JPA, etc.).

How to build and run the application.

## 🍵 Technologies Used 

*   Java 23
*   Spring Boot 3.4.2
*   Spring Data JPA
*   Hibernate
*   Docker
*   PostgreSQL
*   Spring Security (JWT for authentication)
*   Maven
*   Swagger/OpenAPI

## 🏗️ Project Structure

```
src/main/java/com/example/notesapp
├── controller        # REST controllers
│   ├── AuthController.java
│   ├── NoteController.java
│   └── SearchStateController.java
├── dto               # Data Transfer Objects
│   ├── AuthRequestDTO.java
│   ├── AuthResponseDTO.java
│   ├── ErrorDTO.java
│   ├── NoteResponseDTO.java
│   └── ...
├── model             # JPA Entities
│   ├── Note.java
│   ├── Tag.java
│   └── User.java
├── repository        # JPA Repositories
│   ├── NoteRepository.java
│   ├── TagRepository.java
│   └── UserRepository.java
├── service           # Business logic services
│   ├── NoteService.java
│   └── UserService.java
├── security          # Security Configuration
│   ├── JwtService.java
│   └── ...
└── NotesAppApplication.java # Main application class
```
## 🚀 Build and Run

1.  **Clone the repository:** `git clone https://github.com/jcanaviri42/notes-app.git`
2.  **Navigate to the project directory:** `cd notes-app`
3.  **Build the project (Maven):** `mvn clean install` or **(Gradle):** `gradle clean build`
4. **Build the database with docker-compose** `docker-compose up -d`
5. **Run the application:** `mvn spring-boot:run` or `gradle bootRun`

## 🚪 API Endpoints

The API uses standard HTTP methods (GET, POST, PUT, DELETE) and returns JSON responses.

**Authentication:**

*   `/auth/login`: POST - Authenticates a user and returns a JWT.
    *   Request body: `{ "username": "your_username", "password": "your_password" }`
    *   Response body: `{ "token": "your_jwt_token" }`
*   `/auth/register`: POST - Registers a new user.
    *   Request body: `{ "username": "new_username", "password": "new_password", "roles": ["USER"] }`
    *   Response body: `{ "id": 1, "username": "new_username", "roles":["USER"] }`

**Notes:**

*   `/api/notes`: GET - Retrieves all notes for the authenticated user.
*   `/api/notes/{id}`: GET - Retrieves a specific note by ID.
*   `/api/notes`: POST - Creates a new note.
*   `/api/notes/{id}`: PUT - Updates an existing note.
*   `/api/notes/{id}`: DELETE - Deletes a note.
*   `/api/notes/{id}/tags`: PUT - Adds tags to a note. Request body: `{ "tagIds": [1, 2, 3] }`
*   `/api/notes/tag/{tagId}`: GET - Retrieves all notes associated with a specific tag ID.
* `/api/notes/tag/name/{tagName}`: GET - Retrieves all notes associated with a specific tag name.

**Search:**

*   `/api/notes/search`: GET - Performs advanced search on notes. Query parameters:
    *   `title` (optional): Search by title (using `LIKE %title%`).
    *   `content` (optional): Search by content (using `LIKE %content%`).
    *   `tagIds` (optional): Search by tag IDs (multiple values allowed).
    * `tagNames` (optional): Search by tag Names (multiple values allowed).

**User Search State:**

*   `/api/user/search-state`: POST - Saves the user's search state.
*   `/api/user/search-state`: GET - Retrieves the user's saved search state.
*   `/api/user/search-state`: DELETE - Clears the user's saved search state.

**Example Request (Get all notes):**

GET /api/notes
Authorization: Bearer <your_jwt_token>


**Example Response (Get all notes):**

```json
[
  {
    "id": 1,
    "title": "My first note",
    "content": "This is the content of my first note.",
    "isArchived": false,
    "tags": [
        {"id": 1, "name": "Work"},
        {"id": 2, "name": "Personal"}
        ]
  },
  {
    "id": 2,
    "title": "Another note",
    "content": "Some other content.",
    "isArchived": true,
    "tags": []
  }
]
```
```JSON
{
  "message": "Error message"
}
```
with appropriate HTTP status codes (e.g., 400 Bad Request, 404 Not Found, 403 Forbidden, 500 Internal Server Error).

Security
The API uses JWT (JSON Web Tokens) for authentication. All protected endpoints require a valid JWT in the Authorization header (Bearer token).

## 🎨 Design Decisions
DTOs: Data Transfer Objects are used to decouple the API from the internal entities, providing a more stable API contract.

Transactions: @Transactional annotation is used on service methods to ensure data consistency and atomicity.

JPA Specifications: Used for dynamic and complex search queries.

RESTful Principles: The API follows RESTful principles, using standard HTTP
methods and status codes.

## 🤔 Further Improvements
* Add pagination to the note retrieval endpoints.
* Add integration tests.
