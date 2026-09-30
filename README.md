# 🔗 URL Shortener using Java Spring Boot

A simple backend-based **URL Shortener application** developed using **Java, Spring Boot, Spring Data JPA, Hibernate, and MySQL**.

The application accepts a long URL through a REST API, generates a short code, stores the mapping between the short code and the original URL in MySQL, and provides a short URL that can be opened in a web browser to redirect the user to the original website.

---

## 📌 Project Overview

A URL Shortener converts a long URL into a shorter URL.

### Example

Long URL:

```text
https://www.firefox.com
````

After sending the URL to the application, it generates a short URL such as:

```text
http://localhost:8080/aab63a
```

The short URL is returned by the POST API.

The user can then copy the returned short URL and paste it into a web browser.

The browser sends a GET request to the Spring Boot application. The application searches the short code in the MySQL database, retrieves the original URL, and sends an HTTP 302 redirect.

The browser then opens the original website.

---

# 🎯 Features

* Accepts a long URL through a REST API
* Generates a short code
* Stores the short code and original URL in MySQL
* Retrieves the original URL using the short code
* Provides a REST GET endpoint for short URLs
* Redirects the browser to the original URL
* Handles invalid/non-existing short codes with `404 Not Found`
* Can be tested using Postman
* Uses a layered Spring Boot architecture

---

# 🛠️ Technologies Used

| Technology      | Purpose                           |
| --------------- | --------------------------------- |
| Java            | Programming language              |
| Spring Boot     | Backend application framework     |
| Spring Web      | Creating REST APIs                |
| Spring Data JPA | Database interaction              |
| Hibernate       | ORM implementation                |
| MySQL           | Database                          |
| MySQL Workbench | Database management               |
| Maven           | Project and dependency management |
| Postman         | API testing                       |
| Eclipse         | Development environment           |

---

# 🏗️ Architecture

The application follows a simple layered architecture:

```text
                  Client
                /        \
           Postman       Browser
              |             |
              | HTTP        | HTTP
              ↓             ↓
          Controller
              |
              ↓
           Service
              |
              ↓
          Repository
              |
              ↓
       JPA / Hibernate
              |
              ↓
            MySQL
```

### Layers

**Controller**

Handles HTTP requests and responses.

**DTO**

Carries data received from the client.

**Service**

Contains the application's business logic.

**Repository**

Handles database operations through Spring Data JPA.

**Entity**

Represents the URL mapping stored in the database.

**MySQL**

Permanently stores the short code and original URL.

---

# 📁 Project Structure

```text
url-shortener
│
├── src
│   └── main
│       ├── java
│       │   └── com.example.urlshortener
│       │
│       │       ├── UrlShortenerApplication.java
│       │       │
│       │       ├── controller
│       │       │   └── UrlController.java
│       │       │
│       │       ├── dto
│       │       │   └── UrlRequest.java
│       │       │
│       │       ├── entity
│       │       │   └── UrlMapping.java
│       │       │
│       │       ├── repository
│       │       │   └── UrlRepository.java
│       │       │
│       │       └── service
│       │           └── UrlService.java
│       │
│       └── resources
│           └── application.properties
│
├── pom.xml
└── README.md
```

---

# ⚙️ Setup and Installation

## 1. Prerequisites

Install the following:

* Java JDK
* Eclipse IDE
* MySQL
* MySQL Workbench
* Postman
* Maven

---

# 2. Create the Spring Boot Project

Create the project using Spring Initializr.

Select:

```text
Spring Web
Spring Data JPA
MySQL Driver
```

Import the generated project into Eclipse.

---

# 3. Create the Database

Open MySQL Workbench and execute:

```sql
CREATE DATABASE urlshortener;
```

The database is created manually.

The URL table is created automatically by Hibernate when the Spring Boot application starts.

---

# 4. Configure the Database

Open:

```text
src/main/resources/application.properties
```

Add:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/urlshortener
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Replace `YOUR_PASSWORD` with your local MySQL password.

> Do not upload your real database password to GitHub.

---

# 5. Run the Application

Run:

```text
UrlShortenerApplication.java
```

as a Spring Boot application from Eclipse.

Spring Boot starts the application on:

```text
http://localhost:8080
```

---

# 🗄️ Database Structure

The application contains a URL mapping entity.

The database stores information similar to:

```text
+----+------------+--------------------------+
| id | short_code | long_url                 |
+----+------------+--------------------------+
| 1  | aab63a     | https://www.firefox.com  |
+----+------------+--------------------------+
```

The important relationship is:

```text
short code → original URL
```

For example:

```text
aab63a → https://www.firefox.com
```

---

# 🔵 API 1: Create a Short URL

## Endpoint

```text
POST http://localhost:8080/api/shorten
```

This API accepts a long URL and generates a short URL.

---

## Postman Request

Select:

```text
POST
```

URL:

```text
http://localhost:8080/api/shorten
```

Go to:

```text
Body → raw → JSON
```

Send:

```json
{
    "url": "https://www.firefox.com"
}
```

---

## POST Request Flow

The request travels through the application as follows:

```text
Postman
   |
   | HTTP POST
   ↓
/api/shorten
   |
   ↓
UrlController
   |
   ↓
UrlRequest
   |
   ↓
UrlService
   |
   ↓
Generate short code
   |
   ↓
Create UrlMapping object
   |
   ↓
UrlRepository
   |
   ↓
JPA / Hibernate
   |
   ↓
MySQL
```

---

## What Happens Inside the Controller?

The Controller receives:

```json
{
    "url": "https://www.firefox.com"
}
```

using:

```java
@RequestBody UrlRequest request
```

The URL is obtained using:

```java
request.getUrl()
```

which returns:

```text
https://www.firefox.com
```

The Controller then calls:

```java
urlService.shortenUrl(request.getUrl());
```

---

## What Happens Inside the Service?

The Service receives:

```text
https://www.firefox.com
```

It generates a short code.

Example:

```text
aab63a
```

It creates an Entity object containing:

```text
shortCode = aab63a
longUrl   = https://www.firefox.com
```

Then it calls:

```java
urlRepository.save(mapping);
```

The data is stored in MySQL.

Finally, the Service returns:

```text
aab63a
```

---

## POST Response

The Controller creates the short URL:

```text
http://localhost:8080/aab63a
```

and returns it to Postman.

Example response:

```text
http://localhost:8080/aab63a
```

### Important

The POST request **does not directly open the original website**.

It only creates and returns the short URL.

---

# 🟢 API 2: Redirect Using the Short URL

After receiving the short URL from the POST request:

```text
http://localhost:8080/aab63a
```

copy the URL.

Open a web browser and paste the URL into the address bar.

For example:

```text
http://localhost:8080/aab63a
```

Press Enter.

---

# GET Request

The browser sends:

```text
GET http://localhost:8080/aab63a
```

The request is handled by:

```java
@GetMapping("/{shortCode}")
```

The value:

```text
aab63a
```

is received using:

```java
@PathVariable String shortCode
```

Therefore:

```text
shortCode = "aab63a"
```

---

# 🔄 GET Request Flow

```text
Browser
   |
   | GET /aab63a
   ↓
UrlController
   |
   ↓
shortCode = "aab63a"
   |
   ↓
UrlService
   |
   ↓
UrlRepository
   |
   ↓
MySQL
   |
   ↓
Find aab63a
   |
   ↓
https://www.firefox.com
   |
   ↓
Controller
   |
   ↓
HTTP 302 Redirect
   |
   ↓
Browser
   |
   ↓
https://www.firefox.com
```

---

# 🔍 Finding the Original URL

The Controller calls:

```java
urlService.getOriginalUrl(shortCode);
```

The Service calls:

```java
urlRepository.findByShortCode(shortCode);
```

The Repository searches MySQL for:

```text
aab63a
```

MySQL returns:

```text
aab63a → https://www.firefox.com
```

The Service returns:

```text
https://www.firefox.com
```

---

# ↪️ HTTP 302 Redirect

The Controller then sends:

```java
return ResponseEntity
        .status(302)
        .location(URI.create(originalUrl))
        .build();
```

This produces:

```text
HTTP 302
Location: https://www.firefox.com
```

The browser receives the redirect and automatically opens:

```text
https://www.firefox.com
```

---

# 🌐 Complete Project Flow

The complete process is:

```text
                STEP 1
                  ↓
             User enters
              long URL
                  ↓
              Postman
                  ↓
        POST /api/shorten
                  ↓
             Controller
                  ↓
               Service
                  ↓
        Generate short code
                  ↓
             Repository
                  ↓
              MySQL
                  ↓
       Short URL is returned
                  ↓
                STEP 2
                  ↓
        User copies short URL
                  ↓
             Web Browser
                  ↓
        GET /aab63a
                  ↓
             Controller
                  ↓
               Service
                  ↓
             Repository
                  ↓
              MySQL
                  ↓
        Original URL found
                  ↓
            HTTP 302 Redirect
                  ↓
              Web Browser
                  ↓
        Original website opens
```

---

# 🧩 Component-to-Component Communication

## Postman → Controller

Communication happens through:

```text
HTTP
```

Example:

```text
POST /api/shorten
```

---

## Controller → Service

Communication happens through a Java method call:

```java
urlService.shortenUrl(request.getUrl());
```

---

## Service → Repository

Communication happens through a Java method call:

```java
urlRepository.save(mapping);
```

or:

```java
urlRepository.findByShortCode(shortCode);
```

---

## Repository → Database

Spring Data JPA and Hibernate handle communication with MySQL.

```text
Repository
    ↓
JPA
    ↓
Hibernate
    ↓
MySQL
```

---

## Controller → Browser

The Controller sends an HTTP response.

For a successful redirect:

```text
HTTP 302
Location: original URL
```

---

# ❌ Invalid Short URL

If the user opens:

```text
http://localhost:8080/invalid123
```

and `invalid123` does not exist in the database, the Service returns:

```text
null
```

The Controller checks:

```java
if (originalUrl == null) {
    return ResponseEntity.notFound().build();
}
```

The application returns:

```text
404 Not Found
```

---

# 🧠 Important Concepts Demonstrated

This project demonstrates practical usage of:

* Java
* Object-Oriented Programming
* Spring Boot
* REST APIs
* HTTP GET and POST
* JSON
* DTO
* Entity
* Controller
* Service
* Repository
* Dependency Injection
* Spring Data JPA
* Hibernate
* MySQL
* Database connectivity
* Path variables
* Request body
* HTTP status codes
* HTTP 302 redirects
* Postman API testing

---

# 📚 What I Learned From This Project

Through this project, I learned how a backend application works from the client request to the database and back to the client.

The project helped me understand:

1. How to create a Spring Boot application.
2. How to create REST APIs.
3. How Postman communicates with a backend using HTTP.
4. How JSON data is converted into Java objects.
5. How Controllers receive HTTP requests.
6. How Controllers communicate with Services.
7. How Services implement business logic.
8. How Repositories communicate with the database.
9. How JPA and Hibernate simplify database operations.
10. How Java Entities represent database records.
11. How to connect Spring Boot with MySQL.
12. How to generate short URL codes.
13. How to store URL mappings in a database.
14. How to retrieve data using a short code.
15. How HTTP 302 redirection works.
16. How a browser interacts with a backend application.

---

# ⚠️ Note

This is a learning/portfolio implementation of a URL shortener.

The short-code generation currently uses a randomly generated UUID substring. For a production-level application, stronger uniqueness and collision-handling mechanisms should be implemented.

---
## Screenshots

### 1. POST Request – Create Short URL

![POST Request in Postman](https://github.com/user-attachments/assets/75c09da2-582b-4998-a4da-d8db0718a718)

### 2. GET Request – Access Short URL

![GET Request in Postman](https://github.com/user-attachments/assets/d255ff52-741e-4475-826a-912213ca5de7)

### 3. Database – URL Mapping Table

![URL Mapping Table in MySQL](https://github.com/user-attachments/assets/220d4166-7e0f-44ea-9622-b827352f1e0f)
---

# 👩‍💻 Author

**Mahaswetha R**

Java Full Stack Developer | Engineering Student

Github Link: https://github.com/mahaswetha05
