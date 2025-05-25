# Nano_Purse
 A secure digital wallet API built with **Spring Boot**  with Paystack integration for seamless money transfers and financial tracking.

---


📦 Tech Stack
Java 11
Spring Boot
PostgreSQL
Lombok
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

Single-Currency Focus: Primarily handles Nigerian Naira (NGN), with amounts stored in base units (e.g., 100 Naira = 10000 kobo for Paystack).

Moderate Transaction Volume: Designed for typical usage (not high-frequency trading), with optimistic locking for concurrency.

Paystack as Primary Processor: Relies on Paystack for external transfers, with webhook-based status updates.

One Wallet per User: Enforces a strict one-to-one user-wallet relationship for simplicity.

Trusted Client Validation: Input sanitization and security are backend-focused, assuming clients pass validated requests.


---

API Documentation 📖
http://localhost:8081/swagger-ui.html



