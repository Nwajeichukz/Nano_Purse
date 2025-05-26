# Nano_Purse
 A secure digital wallet API built with **Spring Boot**  with Paystack integration for seamless money transfers and financial tracking.

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

Features ✨

JWT authentication and role-based access control

Wallet balance management

Peer-to-peer transfers

Bank transfers via Paystack integration

Transaction history tracking

 Webhook integration for real-time status updates

Transactional integrity with atomic operations

---

Architecture Decisions 🏗️

Key Components
Sql Database: Chosen for ACID compliance in financial transactions

JPA/Hibernate: ORM for simplified data access

Paystack Integration: REST API + webhooks for async updates

Optimistic Locking: Prevents concurrent balance updates

Modular Services: Clean separation of concerns

The NanoPurse Wallet API employs a layered Spring Boot architecture with SQL database persistence, featuring one-to-one user-wallet relationships .

The design separates concerns into controller (HTTP handling), service (business logic including transaction processing), and repository (JPA/Hibernate data access) layers, 

utilizing JWT for authentication, optimistic locking for concurrency control, and DTOs for API contracts. Financial operations maintain ACID compliance through @Transactional atomic processing, 

while supporting both synchronous peer transfers and asynchronous Paystack bank transactions via webhooks. Security is enforced through multi-layered validation, method authorization,

and idempotent operations, with optimized SQL indexes ensuring performance for financial data operations.

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



