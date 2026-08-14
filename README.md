# Online Appointment Management System: fastBooking

A streamlined, dual-sided web application designed to help business owners manage bookings effortlessly while offering customers a frictionless, account-free scheduling experience.

<img width="2536" height="1418" alt="image" src="https://github.com/user-attachments/assets/bd9281ef-15a0-4ff4-8eaa-90555588b5c0" />

## Motivation

Managing appointments should not be a hassle for small business owners, nor should booking one require customers to jump through registration hoops. 

This project bridges that gap by providing:
1. **Business owners** with an intuitive dashboard to set up services and manage schedules.
2. **Customers** with a fast, zero-friction booking experience directly through a shared link.

---

## Key Features

### Business Owner Portal
- **Account Management:** Register and configure organization and business details.
- **Service Configuration:** Define services offered, including durations, pricing, and descriptions.
- **Shareable Booking Link:** Generate a unique public link to share on social media, websites, or messaging apps.
- **Booking Overview:** Dashboard view of upcoming appointments, schedule status, and customer details.

### Customer Experience
- **Frictionless Booking:** No account creation or login required.
- **Service Selection:** Easily browse available services provided by the business.
- **Interactive Calendar:** Pick an available date and time slot in real time.
- **Instant Confirmation:** Get immediate visual confirmation upon completing a booking.

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
- [ ] **Notifications & Reminders:** Automated email confirmations and notifications for upcoming appointments.
- [ ] **Calendar Integration:** Sync with Google Calendar, iCal, and Outlook.
- [ ] **Custom Branding:** Allow business owners to customize colors and logos on their booking page.
