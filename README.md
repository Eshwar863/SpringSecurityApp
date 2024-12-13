
# Secure Android Application with Spring Boot Backend

## Overview
This project is a comprehensive Android application integrated with a Spring Boot backend to manage users and authentication securely. The application demonstrates the integration of modern technologies, ensuring a seamless user experience and robust security measures.

## Features
- **User Management:**
  - User registration
  - Login and logout functionality
  - Password recovery via OTP (One-Time Password)
- **Authentication:**
  - JWT (JSON Web Tokens) for secure authentication and session management
- **User Interface:**
  - Three primary fragments for navigation:
    - **Home:** Displays JWT tokens and user-related data
    - **Users:** Lists all users in the system
    - **Settings:** Allows account customization and management
- **Secure Backend:**
  - Built with Spring Boot and secured using Spring Security

## Technology Stack
### Frontend (Android)
- **Language:** Java
- **HTTP Client:** Retrofit for API integration
- **UI Design:** Android Fragments

### Backend (Spring Boot)
- **Frameworks:**
  - Spring Boot for REST API development
  - Spring Security for authentication and authorization
- **Database:** MySQL 
- **Token Management:** JWT
- **Email Integration:** Java Mail Sender for OTP-based password recovery

## Setup and Installation

### Prerequisites
- **Java Development Kit (JDK):** Version 11 or higher
- **Android Studio:** Latest stable version
- **Spring Boot:** Compatible environment with Maven or Gradle
- **Database:** Set up a compatible database (e.g., MySQL)
- **API Client Tools:** Postman or equivalent for testing APIs

### Backend Setup
1. Clone the repository:
   ```bash
   git clone <https://github.com/Eshwar863/SpringSecurityApp.git>
   ```
2. Navigate to the backend directory:
   ```bash
   cd backend
   ```
3. Configure the application:
   - Update `application.properties` or `application.yml` with your database and email settings.
4. Build and run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
5. Test the APIs using Postman or Swagger UI (if configured).

### Android Setup
1. Open the project in Android Studio.
2. Update the `base_url` in Retrofit configuration to point to your backend.
3. Build and run the application on an emulator or physical device.

## Usage
1. **User Registration:** Sign up as a new user.
2. **Login:** Authenticate using your credentials.
3. **JWT Authentication:** Access secure features using generated JWT tokens.
4. **Password Recovery:** Reset your password via OTP sent to your email.
5. **Navigation:** Switch between Home, Users, and Settings fragments for respective functionalities.

## Future Enhancements
- **Real-time Notifications**
- **Admin Panel for Advanced Management**
- **Payment Gateway Integration**
- **Analytics Dashboard**


## Download APK

You can download the APK from the link below:

[Download APK](https://drive.google.com/drive/folders/1aomlzqTwqCudhS7ZUeiSgc-kLCrIgLk0?usp=sharing)
