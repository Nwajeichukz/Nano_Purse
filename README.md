# Nano_Purse

A secure, scalable digital wallet API built with Spring Boot — featuring seamless Paystack integration for effortless money transfers and comprehensive financial tracking.
---


📦 Tech Stack

Java 11

Spring Boot

PostgreSQL

Lombok

Spring Security

JPA/Hibernate

Maven

---

# ✨ Features & Highlights


JWT Authentication & Role-Based Access Control: Secure, stateless authentication with flexible permission handling.

Wallet Balance Management: Maintain accurate, real-time balances with optimistic locking to prevent race conditions.

Peer-to-Peer Transfers: Fast, atomic transactions between users within the platform.

Bank Transfers via Paystack Integration: Effortlessly send funds externally with full webhook support for asynchronous transaction status updates.

Comprehensive Transaction History: Detailed audit trails for every wallet operation.

Transactional Integrity: All critical operations are atomic and consistent, ensuring financial data safety.

Webhook-Driven Real-Time Updates: Stay synchronized with Paystack events for up-to-the-second status accuracy.

Rate Limiting: Protect the system and users by limiting request frequency on sensitive endpoints.


---

# Architecture Decisions 🏗️

Nano_Purse employs a clean, layered architecture that promotes scalability and maintainability:

Controller Layer: Handles HTTP requests/responses and enforces security rules.

Service Layer: Encapsulates business logic, transaction processing, and external API interactions.

Repository Layer: Manages data persistence through JPA/Hibernate abstractions.

Design Decisions:

ACID-Compliant PostgreSQL Database: Ensures transaction safety and data integrity essential for financial applications.

Optimistic Locking: Prevents concurrent balance update conflicts without heavy locking overhead.

Modular Service Design: Separates concerns clearly, enabling easier testing and feature expansion.

JWT-Based Security: Supports stateless sessions with role-based access control for fine-grained authorization.

DTO Usage: Enforces clear API contracts and reduces exposure of internal entities.

Efficient SQL Indexing: Optimizes query performance for high-throughput wallet operations.

---


ASSUMPTIONS:


Single-Currency Support: The application is tailored for the Nigerian Naira (NGN), with all monetary values stored in base units 
(e.g., ₦100 is represented as 10,000 kobo, in line with Paystack's standards).

Moderate Transaction Load: Optimized for standard usage scenarios rather than high-frequency trading. 
It employs optimistic locking to ensure safe concurrent updates.

Paystack Integration for External Transfers: Paystack serves as the primary payment gateway for handling external transactions. 
The system uses webhooks to receive and process transaction status updates reliably.

Transaction Fees: For every external transfer, an estimated transaction fee is automatically applied based on the transfer amount.
This ensures transparency and helps cover processing costs.

Strict One-to-One Wallet Mapping: Each user is associated with exactly one wallet, ensuring a simplified and consistent wallet management model.

Backend-Centric Validation: Security and input validation are handled primarily at the backend, under the assumption that requests from clients have already been pre-validated and sanitized.

Rate Limiting: To protect system stability and prevent abuse, rate limiting is enforced on critical endpoints, controlling the number of allowed requests per user within defined time windows 
in line with the moderate transaction load assumption.




---

📋 Prerequisites

Ensure the following tools are installed on your machine before running the application:

Java 11 or newer (required for building and running the Spring Boot application)

Maven (for dependency management and building)

PostgreSQL (for database)

Git (to clone the repository)

Install Java 11

Download and install from Oracle’s official website or use your preferred package manager.

Install Maven

Download Maven from Maven's official website or use package managers like apt (Linux), brew (Mac), etc.

Install PostgreSQL

Install PostgreSQL from PostgreSQL’s official website.


⚙️ Build and Run

1. Clone the Repository
git clone https://github.com/your-username/Nano_Purse.git

cd Nano_Purse


---


API Documentation 📖
http://localhost:8081/swagger-ui.html



