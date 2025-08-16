Bank Management System[Ongoing]

This is a full-stack Bank Management System built with Spring Boot (Java) for the backend and Angular for the frontend. The project includes core banking features such as account creation, login with JWT, OTP verification, money transfer, transaction history, and secure PIN reset.

Technologies Used

Backend
- Java 17
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- MySQL
- Twilio (for SMS OTP - simulated)
- JavaMailSender (for Email OTP)

Frontend
- Angular 19
- Angular Forms and Reactive Forms
- Angular Routing
- PrimeNG (UI Components)

Features

- Create a bank account
- Login with JWT token
- Send OTP via email and phone for PIN reset
- Change PIN after OTP verification
- Transfer money between accounts
- View account details
- View transaction history with before/after balances

Project Structure

Backend (Spring Boot)
- `controller` - API endpoints
- `service` and `serviceImpl` - Business logic
- `model` - Entity and DTO classes
- `repository` - Database operations
- `security` - JWT and Spring Security config

Frontend (Angular)
- `pages/` - UI pages like login, dashboard, transfer, etc.
- `services/` - API calls
- `public-layout/` and `private-layout/` - Layouts for routing
  
