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
Google OAuth2 Login Page opens
        ↓
User signs in with Google account
        ↓
Google redirects to → /auth/oauth-success
        ↓
Backend verifies Google account details
        ↓
If first login → Save user in DB
        ↓
Backend generates JWT token
        ↓
Backend stores JWT in HttpOnly cookie (JWT_TOKEN)
        ↓
User is now authenticated in backend
        ↓
User now accesses → /facebook/login (manually or via UI)
        ↓
JwtFilter validates the JWT → user allowed
        ↓
Backend redirects user to Facebook OAuth:
        https://www.facebook.com/v19.0/dialog/oauth?...
        ↓
User logs in with Facebook
        ↓
Facebook asks permissions:
        • pages_show_list
        • pages_read_engagement
        • pages_manage_posts
        ↓
User clicks “Allow”
        ↓
Facebook redirects to:
        /facebook/callback?code=XXXX&state=APP_USER_ID
        ↓
Backend exchanges code → Facebook user token
        ↓
Backend fetches user details using the token:
        GET /me?fields=id,name,email
        ↓
Backend encrypts token (AES)
        ↓
Save FacebookUser to DB
        ↓
Redirect user to /facebook/pages
        ↓
Backend fetches managed pages:
        GET /me/accounts
        ↓
Return page list to frontend
        ↓
User sends selected pages to backend(to save in db)using 
        POST /facebook/save-pages
        ↓
Encrypt & store page access tokens
        ↓
User can now publish posts using:
        /facebook/post-text
        /facebook/post-image
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
CREATE DATABASE flintzy;
```

### 3️⃣ Update `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/flintzy
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

facebook.app.id=YOUR_APP_ID
facebook.app.secret=YOUR_APP_SECRET
facebook.redirect.uri=http://localhost:8080/facebook/callback
facebook.api.version=v24.0

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
│
├── controller/
│   ├── AuthController.java
│   ├── FacebookController.java
│
├── service/
│   ├── FacebookPostingService.java     
│   ├── FacebookTokenService.java       
|               
│
├── dto/
│   ├── FacebookPageDTO.java            
│   ├── FacebookPageRequest.java       
│   ├── PostRequest.java                
│   ├── JwtResponse.java               
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
|
├── security/
│   ├── AESEncryptor.java
│
└── FlintzyBackendApplication.java



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
Authorization: Bearer <JWT>
```

### 🔹 Facebook Callback
```
GET /facebook/callback?code=xxxx&state=APP_USER_ID
Authorization: Bearer <JWT>
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

Form Data:
file=@image.jpg
caption={YOUR_CAPTION}
pageId = {pageId}

```
### 📡 Facebook Graph API Integration (Implemented)

This backend fully integrates Facebook Graph API as required in the assignment.
All features — login, page linking, and post publishing — are handled using secure Graph API calls from the backend.

Below is a clear mapping of the Graph API endpoints used and the backend methods that call them.

🔗 Graph API Endpoints Used in This Project

### 1️⃣ Facebook OAuth Login — Authenticate User
Used to authenticate Facebook users and obtain a User Access Token.
```bash
GET https://www.facebook.com/v19.0/dialog/oauth
    ?client_id=YOUR_APP_ID
    &redirect_uri=http://localhost:8080/facebook/callback
    &scope=public_profile,email,pages_manage_posts,pages_read_engagement,pages_show_list
    &state=APP_USER_ID
```

### Code Reference

FacebookController.facebookLogin()

### 2️⃣ Exchange Code → Facebook User Access Token

After Facebook redirects to /facebook/callback, the backend exchanges the code for a token.
```bash
GET https://graph.facebook.com/v19.0/oauth/access_token
    ?client_id=YOUR_APP_ID
    &redirect_uri=http://localhost:8080/facebook/callback
    &client_secret=YOUR_APP_SECRET
    &code=AUTHORIZATION_CODE
```

### Code Reference

FacebookController.facebookCallback()

### 3️⃣ Fetch Facebook User Details (id, name, email)

Your code calls /me without version, so the default Graph API version is used.
```bash
GET https://graph.facebook.com/me?fields=id,name,email&access_token=USER_ACCESS_TOKEN
```

### Code Reference

FacebookController.facebookCallback()


### 4️⃣ Fetch Facebook Pages Managed by User

Retrieve all Facebook Pages that the authenticated user can manage.
```bash
GET https://graph.facebook.com/me/accounts?access_token=USER_ACCESS_TOKEN
```

Returns Page ID, Page Name & Page Access Token.

### Code Reference:
FacebookController.getPages()

### 5️⃣ Publish Text Post to Facebook Page

Used to publish text posts to a Page.
```bash
POST https://graph.facebook.com/{PAGE_ID}/feed
```
Query Params:

message=YOUR_MESSAGE
access_token=PAGE_ACCESS_TOKEN

### Code Reference:
FacebookPostingService.publishTextPost()

### 6️⃣ Publish Image Post to Facebook Page
```bash
POST https://graph.facebook.com/{PAGE_ID}/photos
```
### Form Data:

file=@image.jpg
caption=YOUR_CAPTION
access_token=PAGE_ACCESS_TOKEN


### Code Reference:
FacebookPostingService.publishImagePost()

---

## 🐚 cURL Commands

### Login (Google)
```bash
curl -X GET http://localhost:8080/oauth2/authorization/google
```
### Login (Facebook)
```
curl -X GET http://localhost:8080/facebook/login \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwc2hpdnJhbWlpdEBnbWFpbC5jb20iLCJpYXQiOjE3NjcxNzA0MTMsImV4cCI6MTc2NzE3NDAxM30.v0I0QvrGkAj0Lj-1FkmZtkw5SkLBf9VNWbvpXZ1KQs0"
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
-H "Authorization: Bearer <TOKEN>" \
-d '{
  "pages": [
    {
      "pageId": "{page_Id}",
      "pageName": "{Page_Name}",
      "pageAccessToken": "PAGE_ACCESS_TOKEN"
    }
  ]
}'
```
### Post Text to Facebook Page
```bash
curl -X POST http://localhost:8080/facebook/post-text \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {JWT_TOKEN}" \
  -d '{
    "pageId": "{page_Id}",
    "message": "{message}"
  }'
```
### Post Image to Facebook Page
```bash
curl -X POST http://localhost:8080/facebook/post-image \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -F "pageId=123456789" \
  -F "file=@/path/to/image.jpg" \
  -F "caption=Posting image from Flintzy Backend"
```
---n=Posting image from Flintzy Backend"
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

### Google OAuth Setup (Required)
1️⃣ Open Google Cloud Console

https://console.cloud.google.com/

### 2️⃣ Create Project → Enable OAuth APIs
### 3️⃣ Configure OAuth Consent Screen
### 4️⃣ Create OAuth Client Credentials

Go to:

APIs & Services → Credentials → Create Credentials → OAuth Client ID

###5️⃣ Add Redirect URI
http://localhost:8080/login/oauth2/code/google

### 6️⃣ Add Credentials in Properties
spring.security.oauth2.client.registration.google.client-id=XXXX
spring.security.oauth2.client.registration.google.client-secret=XXXX

### 📘 Facebook OAuth Setup (Required)

To enable Facebook Login, Page access, and posting via Graph API, you must configure your Facebook App properly.

### 1️⃣ Create a Facebook Developer Account

If you haven’t registered:

🔗 https://developers.facebook.com/

Click "Get Started" → Continue → Verify account.

### 2️⃣ Create a New App

Go to 👉 https://developers.facebook.com/apps

Click Create App
Select “Other”
App Type → Business
Enter:
App Name
Contact Email
Create App

### 3️⃣ Add Product → Facebook Login

In the left menu → Add Product
Choose Facebook Login
Select platform → Web
Enter Site URL:
http://localhost:8080

### 4️⃣ Add Valid OAuth Redirect URIs

Navigate:
Facebook Login → Settings
Add the following:
http://localhost:8080/facebook/callback
http://localhost:8080/login/oauth2/code/facebook


### Required
Your backend uses facebook.redirect.uri=http://localhost:8080/facebook/callback
so it must be added here.

### 5️⃣ Set App Domains

Go to: Settings → Basic

Add:

localhost


Note: Must have a top-level domain.
For local dev, just localhost is allowed.

### 6️⃣ Add Privacy Policy URL (Required)

Facebook requires a valid URL.
For development, you can use a temporary free URL:

https://example.com/privacy


or create one using GitHub Pages, Netlify, Vercel, etc.

### 7️⃣ Get App Credentials

From Settings → Basic:

App ID

App Secret

Add them into your application.properties:

facebook.app.id=YOUR_APP_ID
facebook.app.secret=YOUR_APP_SECRET
facebook.redirect.uri=http://localhost:8080/facebook/callback
facebook.api.version=v19.0

### 8️⃣ Add Required Permissions for Posting

Go to:
App → App Review → Permissions & Features

Search and enable these (Standard Access is enough):

Required permissions
Permission	Purpose
pages_show_list	Fetch pages
pages_read_engagement	Read page details
pages_manage_posts	Publish page posts

All three must show:

Status: Ready to Use (0)
Access Level: Standard Access

### 9️⃣ Test User Setup

Go to → Roles → Test Users

Add a test user

Login with this test user on Facebook

Assign this user as:

Admin of the test Page

Tester of the App

⚠️ This step is required for publishing posts during development.

### 🔟 Testing Facebook Login

When your backend calls:

GET /facebook/login


The user should see:

Facebook login screen

Permission dialog

Page selection list

After approval → redirects to:

/facebook/callback?code=XXXX&state=APP_USER_ID

### 1️⃣1️⃣ Verify Page Access Token

Call:

GET https://graph.facebook.com/me/accounts?access_token=USER_ACCESS_TOKEN


You should receive:

[
  {
    "id": "PAGE_ID",
    "name": "Page Name",
    "access_token": "PAGE_ACCESS_TOKEN"
  }
]


Your backend will:

Encrypt PAGE_ACCESS_TOKEN

Store it in DB

Use it for posting

### 1️⃣2️⃣ Facebook Post API Requirements

Facebook will allow publishing ONLY IF:

Token belongs to a user who is admin of the Page

Permissions granted:

### pages_manage_posts

### pages_read_engagement

### Token is a Page Access Token, NOT User token

### 🎉 Facebook OAuth Setup Completed

