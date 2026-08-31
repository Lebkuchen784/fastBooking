# Online Appointment Management System: fastBooking

A straight-forward, dual-sided web application designed to help business owners manage bookings effortlessly while offering customers a frictionless, account-free scheduling experience.

<img width="2879" height="1668" alt="screenshot" src="https://github.com/user-attachments/assets/00ae86af-9565-406a-979d-a244422204db" />

## Motivation

Managing appointments should not be a hassle for small business owners, nor should booking one require customers to jump through registration hoops. 

This project bridges that gap by providing:
1. **Business owners** with an intuitive dashboard to go through and manage bookings.
2. **Customers** with a fast, zero-friction booking experience directly through a shared link, requiring no account creation.

---

## Key Features

### Business Owner Portal
- **Account Management:** Register and configure organization and business details.
- **Service Configuration:** Define services offered, including durations, pricing, and descriptions.
- **Shareable Booking Link:** Generate a unique public link to share on social media, websites, or messaging apps.
- **Booking Overview:** Dashboard view of upcoming appointments, schedule status, and customer details.

### Customer Experience
- **Frictionless Booking:** No account creation or login required.
- **Interactive Calendar:** Pick an available date and time slot in real time.
- **Instant Confirmation:** Get immediate Email confirmation upon completing a booking along with a cancellation link.

---

## Architecture & Technologies

This application is built using a modern Java tech stack, incorporating batch processing concepts:

- **Backend Framework:** Spring Boot (Java 21)
- **Data Access:** Spring Data JPA, Hibernate, Spring JDBC
- **Database:** PostgreSQL
- **Security:** Spring Security, JWT (JSON Web Tokens)
- **Frontend / Templating:** Thymeleaf
- **Batch Processing:** Spring Batch
- **API Documentation & Testing:** Spring REST Docs, Spring Boot Test

---

## Getting Started

### Prerequisites
- Java 21 or higher
- PostgreSQL
- Maven

### Installation & Running Locally

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```

2. Configure the database properties in your `application.properties` or `application.yml`.

3. Build and run the application using Maven:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

---

## Future Roadmap

- [ ] **Staff Management:** Allow owners to assign specific team members to services and schedules.
- [ ] **Custom Branding:** Allow business owners to customize colors and logos on their booking page.
