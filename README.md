# SplitApp Backend

A **Spring Boot** (Java) application for splitting group expenses among participants, using **MongoDB** for persistence. This repository provides RESTful APIs, error handling, settlement logic, analytics endpoints, and a ready-to-import Postman collection for immediate testing.

---

## Table of Contents

1. [Features](#features)
2. [Prerequisites](#prerequisites)
3. [Setup & Configuration](#setup--configuration)

   * Application Properties
   * MongoDB Connection
4. [Build & Run Locally](#build--run-locally)
5. [API Endpoints](#api-endpoints)

   * Expense Management
   * Analytics
   * Settlements & People
   * Health Check
6. [Postman Collection](#postman-collection)

   * Import Instructions
   * Collection Variables
   * Running the Collection
7. [Edge Cases & Validation](#edge-cases--validation)
---

## Features

* Create, read, update, delete (CRUD) **expenses** with **date** and **category** fields
* Calculate **net balances** per person
* Generate **optimized settlement** transactions
* **Analytics endpoints**: monthly spending summary and top N categories
* Global exception handling with clear JSON responses
* Data validation using JSR-380 annotations

---

## Prerequisites

* Java 17 or later
* Maven 3.6+
* MongoDB Atlas account or local MongoDB instance
* (Optional) Postman for API testing

---

## Setup & Configuration

### 1. Application Properties

Copy and update `src/main/resources/application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI}

server:
  port: 8080
```

* On local, replace `${MONGODB_URI}` with your Atlas URI:
  `mongodb+srv://<user>:<pass>@<cluster>.mongodb.net/splitapp?retryWrites=true&w=majority`
* In production (e.g. Railway), the `MONGODB_URI` will be provided as an environment variable.


2. **Local**: install and start MongoDB on `localhost:27017`

---

## Build & Run Locally

```bash
# Clone the repo
git clone https://github.com/qusaii21/splitapp.git

# Configure MongoDB URI in application.yml
# Build and run
mvn clean package
mvn spring-boot:run
```

The server will start on `http://localhost:8080`.

---

## API Endpoints

### Expense Management

| Method | Path                                | Description                                 |
| ------ | ----------------------------------- | ------------------------------------------- |
| POST   | `/api/expenses`                     | Create new expense (with date and category) |
| GET    | `/api/expenses`                     | List all expenses                           |
| GET    | `/api/expenses/{id}`                | Get expense by ID                           |
| GET    | `/api/expenses/category/{category}` | List expenses by category                   |
| PUT    | `/api/expenses/{id}`                | Update expense                              |
| DELETE | `/api/expenses/{id}`                | Delete expense                              |

### Analytics

| Method | Path                                                       | Description               |
| ------ | ---------------------------------------------------------- | ------------------------- |
| GET    | `/api/analytics/monthly-summary?year={year}&month={month}` | Monthly spending summary  |
| GET    | `/api/analytics/top-categories?n={topN}`                   | Top N spending categories |

### Settlements & People

| Method | Path               | Description                        |
| ------ | ------------------ | ---------------------------------- |
| GET    | `/api/people`      | List all unique people             |
| GET    | `/api/balances`    | Calculate net balances per person  |
| GET    | `/api/settlements` | Generate optimized settlement list |

### Health Check

| Method | Path          | Description          |
| ------ | ------------- | -------------------- |
| GET    | `/api/health` | Service health check |

---

## Postman Collection

A pre-built Postman collection (`SplitApp.postman_collection.json`) is provided. It includes:

* Demo data for **Shantanu**, **Sanket**, and **Om**
* Full request bodies with **date** and **category** fields
* Analytics and category test cases
* Organized folders

### Import Instructions

1. Open Postman → **Import** → **File** → select `SplitApp.postman_collection.json`.
2. Set the environment variable **`baseUrl`** to your deployed or local API URL (e.g., `http://localhost:8080`).

### Collection Variables

| Variable   | Description                                                              |
| ---------- | ------------------------------------------------------------------------ |
| `baseUrl`  | Base API URL (e.g., `http://localhost:8080`)                             |
| `petrolId` | ID of the Petrol expense (captured after POST)                           |
| `pizzaId`  | ID of the Pizza expense (captured after POST)                            |
| `category` | Category to filter by (e.g., `FOOD`, `TRAVEL`, `ENTERTAINMENT`, `OTHER`) |
| `year`     | Year for monthly summary (e.g., `2025`)                                  |
| `month`    | Month for monthly summary (1–12)                                         |
| `topN`     | Number of top categories to retrieve (e.g., `3`)                         |

> **Tip**: After running the initial POST requests in the **Expense Management** folder, copy the returned IDs into the `petrolId` and `pizzaId` variables under the **Env > Collections** tab so that update/delete requests work correctly.

### Running the Collection

1. Run **Expense Management** folder in order.
2. Verify `petrolId` and `pizzaId` are set.
3. Run **Analytics** to see monthly summary and top categories.
4. Run **Settlements & People** to see balances and settlements.
5. Run **Edge Cases & Validation** to confirm error handling.

---

## Edge Cases & Validation

* Negative or zero `amount` → HTTP 400 Bad Request
* Empty `description` or missing `paidBy`, `date`, or `category` → HTTP 400
* Update/Delete non-existent ID → HTTP 404 Not Found
* GET balances with no expenses → returns empty list


