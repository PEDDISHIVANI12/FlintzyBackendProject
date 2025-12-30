# 🚀 Flintzy Backend Project

A **Spring Boot–based Social Media Management Backend** that allows users to securely log in using **Google OAuth2**, connect their **Facebook Pages** via the Facebook Graph API, and publish posts directly from the backend to Facebook.

All APIs are protected using **JWT authentication**, and **MySQL** is used as the persistent data store. Facebook access tokens are securely stored using **AES encryption**.

---

## 🧠 Overview

**Flintzy Backend API**  
Google–Facebook OAuth Integration + Social Media Automation Backend

This backend enables users to:

- Log in with Google OAuth2
- Connect their Facebook account
- Fetch Facebook Pages
- Save selected pages to the database
- Publish text & image posts to Facebook Pages
- Store Facebook access tokens securely (AES encryption)
- Use JWT-based authentication for secure API calls

**Backend built using:**  
Spring Boot 3, OAuth2, Spring Security, JPA, JWT, AES Encryption

---

## 📌 Table of Contents

- ✨ Features
- 🛠 Tech Stack
- 📐 Architecture Diagram
- 🔑 OAuth2 Login Flow
- ⚙️ Setup Instructions
- 📁 Folder Structure
- 🔐 Security & Authentication
- 📘 API Documentation
- 🐚 cURL Commands
- 🗄 Database Schema

---

## ✨ Features

### 🔐 User Authentication
- Login using Google OAuth2
- JWT token generation after successful login
- Secure HttpOnly cookie storage (`JWT_TOKEN`)
- Protected APIs using JWT validation

### 🔗 Facebook Integration
- Connect Facebook account
- Retrieve Facebook Pages
- Save selected pages to database
- Publish text and image posts to Facebook Pages

### 🔒 Encryption
- Facebook user tokens stored using AES encryption
- Page access tokens encrypted before persisting

### 🔁 Token Expiry Management
- Facebook user token expiry tracking

---

## 🛠 Tech Stack

| Component  | Technology |
|----------|------------|
| Backend  | Spring Boot 3 |
| Security | Spring Security + OAuth2 |
| Auth     | Google OAuth2, Facebook OAuth |
| Database | MySQL |
| Build Tool | Maven |
| Encryption | AES-256 |
| Token | JWT |

---

## 🔑 OAuth2 Login Flow (Google → Facebook)

```
User clicks Login
        ↓
Google OAuth2 Login
        ↓
/auth/oauth-success
        ↓ (JWT cookie created)
User Authenticated
        ↓
User clicks "Connect Facebook"
        ↓
Facebook OAuth Login
        ↓
Facebook callback → /facebook/callback
        ↓
Store token securely
        ↓
Redirect to /facebook/pages
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the repository
```bash
git clone <your-repo-url>
cd flintzy-backend
```

### 2️⃣ Configure MySQL
```sql
CREATE DATABASE flintzy_db;
```

### 3️⃣ Update `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/flintzy_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

facebook.app.id=YOUR_APP_ID
facebook.app.secret=YOUR_APP_SECRET
facebook.redirect.uri=http://localhost:8080/facebook/callback
facebook.api.version=v19.0

jwt.secret=GENERATE_32_BYTE_SECRET
```

### 4️⃣ Add Google OAuth credentials
```properties
spring.security.oauth2.client.registration.google.client-id=XXXX
spring.security.oauth2.client.registration.google.client-secret=XXXX
```

### 5️⃣ Run the project
```bash
mvn spring-boot:run
```

---

## 📁 Folder Structure

```
src/main/java/com/flintzy
│
├── config/
│   ├── SecurityConfig.java
│   ├── JwtFilter.java
│   ├── JwtUtil.java
│
├── controller/
│   ├── AuthController.java
│   ├── FacebookController.java
│
├── service/
│   ├── FacebookPostingService.java
│   ├── FacebookTokenService.java
│
├── entity/
│   ├── User.java
│   ├── FacebookUser.java
│   ├── FacebookPage.java
│   ├── FacebookPost.java
│
├── repo/
│   ├── UserRepo.java
│   ├── FacebookUserRepo.java
│   ├── FacebookPageRepo.java
│   ├── FacebookPostRepo.java


```

---

## 🔐 Security & Authentication

- `/auth/**` → Public endpoints (Google OAuth)
- `/facebook/**` → Secured endpoints
- JWT validated via:
  - `Authorization: Bearer <token>` header  
  - OR `JWT_TOKEN` HttpOnly cookie

---

## 📘 API Documentation

### 🔹 Google Login
```
GET /oauth2/authorization/google
```

### 🔹 Facebook Login
```
GET /facebook/login
```

### 🔹 Facebook Callback
```
GET /facebook/callback?code=xxxx&state=APP_USER_ID
```

### 🔹 Get Facebook Pages
```
GET /facebook/pages
Authorization: Bearer <JWT>
```

### 🔹 Save Pages
```
POST /facebook/save-pages
Authorization: Bearer <JWT>
```

### 🔹 Post Text
```
POST /facebook/post-text?pageId={PAGE_ID}&message=Hello
Authorization: Bearer <JWT>
```

### 🔹 Post Image
```
POST /facebook/post-image
Authorization: Bearer <JWT>
```

---

## 🐚 cURL Commands

### Login (Google)
```bash
curl -X GET http://localhost:8080/oauth2/authorization/google
```

### Get Pages
```bash
curl -X GET http://localhost:8080/facebook/pages \
-H "Authorization: Bearer <TOKEN>"
```

### Save Pages
```bash
curl -X POST http://localhost:8080/facebook/save-pages \
-H "Content-Type: application/json" \
-d '{
  "pages": [
    {
      "pageId": "123",
      "pageName": "Test Page",
      "accessToken": "TOKEN"
    }
  ]
}'
```

---

 Database Schema

### 🧑 User
| Field       | Type    |
|------------|---------|
| appUserId  | BIGINT  |
| email      | VARCHAR |
| name       | VARCHAR |

---

### 🔵 FacebookUser
| Field           | Type              |
|----------------|-------------------|
| id             | BIGINT (PK)       |
| facebookUserId | VARCHAR           |
| appUserId      | BIGINT (FK)       |
| accessToken    | TEXT (Encrypted)  |
| expirySeconds  | INT               |
| expiryTime     | DATETIME          |
| lastUpdated    | DATETIME          |

---

### 📄 FacebookPage
| Field           | Type              |
|----------------|-------------------|
| id             | BIGINT (PK)       |
| pageId         | VARCHAR           |
| pageName       | VARCHAR           |
| accessToken    | TEXT (Encrypted)  |
| facebookUserId | VARCHAR           |
| appUserId      | BIGINT (FK)       |
| lastUpdated    | DATETIME          |

---

### 📝 FacebookPost
| Field            | Type        |
|------------------|-------------|
| id               | BIGINT (PK) |
| pageId           | VARCHAR     |
| appUserId        | BIGINT (FK) |
| facebookUserId   | VARCHAR     |
| facebookPostId   | VARCHAR     |
| caption          | TEXT        |
| mediaType        | VARCHAR     |
| createdAt        | DATETIME   |


### ⚠ Database Dump Notice

The SQL files included in this repository contain:
- Full database schema
- Sanitized sample data only

No real user data, access tokens, OAuth secrets, or credentials are included.
All sensitive values have been masked for security reasons.

