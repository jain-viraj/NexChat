# 💬 NexChat - Real-Time One-to-One Chat Application

NexChat is a scalable, real-time 1-on-1 messaging platform built with **Spring Boot 3**, **Spring WebSocket (STOMP)**, **Spring Security**, **JWT Authentication**, and **MySQL**. It features a modern web client with secure user registration, token-based authentication, user directory discovery, dynamic paginated message history, and instant message delivery.

---

## ✨ Features

- 🔐 **JWT Authentication & Security**: Secure user registration & login using BCrypt password hashing and JWT token authorization.
- ⚡ **Real-time 1-on-1 Messaging**: Instant STOMP over WebSocket communication with user-specific destination routing (`/queue/messages/{username}`).
- 📜 **Paginated Chat History**: Efficient database retrieval with pagination support to lazily load historical messages.
- 👥 **User Discovery**: View registered users and start active messaging sessions seamlessly.
- 🗄️ **Persistent Data Storage**: Robust MySQL integration via Spring Data JPA and Hibernate ORM.
- 🎨 **Responsive Frontend**: Clean web interface built with SockJS, STOMP.js, and CSS glassmorphism styling.

---

## 🛠️ Tech Stack

### **Backend**
- **Language**: Java 17
- **Framework**: Spring Boot 3.2.0
- **Security**: Spring Security, JJWT (`io.jsonwebtoken:0.11.5`), BCrypt
- **Real-Time Engine**: Spring WebSocket, STOMP Messaging
- **Database**: MySQL 8.x
- **ORM / Data Access**: Spring Data JPA / Hibernate
- **Utilities**: Lombok, Maven

### **Frontend**
- **Technologies**: HTML5, CSS3, JavaScript (ES6+)
- **Protocols & Libraries**: SockJS Client, STOMP.js

---

## 📁 Directory Structure

```text
NexChat/
├── pom.xml
├── mvnw / mvnw.cmd
├── src/
│   ├── main/
│   │   ├── java/com/example/chatapp/
│   │   │   ├── ChatAppApplication.java
│   │   │   ├── config/
│   │   │   │   ├── ScalableWebSocketConfig.java
│   │   │   │   └── WebSocketConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── ChatWebSocketController.java
│   │   │   │   └── UserRestController.java
│   │   │   ├── dto/
│   │   │   │   └── ChatMessageDto.java
│   │   │   ├── entity/
│   │   │   │   ├── ChatMessage.java
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   │   ├── ChatMessageRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── security/
│   │   │   │   ├── JwtRequestFilter.java
│   │   │   │   ├── JwtUtil.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   └── service/
│   │   │       ├── ChatMessageService.java
│   │   │       └── UserService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           └── index.html
```

---

## 🚀 Getting Started

### 📋 Prerequisites

Make sure you have the following installed on your machine:
- **Java Development Kit (JDK)** 17 or higher
- **Maven** 3.8+ (or use the included `mvnw` wrapper)
- **MySQL Server** 8.0+ running on `localhost:3306`

---

## ⚙️ Configuration

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chat_app?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

server.port=8080
```

---

## 🏃 Running the Application

1. **Clone the Repository**
   ```bash
   git clone https://github.com/jain-viraj/NexChat.git
   cd NexChat
   ```

2. **Build the Application**
   ```bash
   ./mvnw clean package
   ```

3. **Run the Spring Boot Server**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the Application**
   Open your browser and navigate to:
   ```text
   http://localhost:8080
   ```

---

## 📡 API Reference

### 🔑 Authentication & User Endpoints

| Method | Endpoint | Description | Request Body / Query |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/register` | Register a new user | `{ "username": "alice", "password": "123" }` |
| `POST` | `/api/login` | Authenticate & get JWT token | `{ "username": "alice", "password": "123" }` |
| `GET` | `/api/users` | List all registered usernames | *Requires Bearer Token* |
| `GET` | `/api/history` | Get paginated chat history | `user=alice&partner=bob&page=0&size=20` |

### 🔌 WebSocket Protocol Specs

- **Endpoint**: `/ws` (with SockJS fallback enabled)
- **Send Message Destination**: `/app/chat.send`
  ```json
  {
    "sender": "alice",
    "receiver": "bob",
    "content": "Hello Bob!"
  }
  ```
- **Subscribe Destination**: `/queue/messages/{username}`

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for details.
