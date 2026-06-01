# NAV Tracker

A backend REST API built with Java and Spring Boot for tracking mutual fund Net Asset Values (NAV). Supports bulk data ingestion from AMFI-format `.txt` files and exposes endpoints to query the latest NAV, historical NAV over a date range, and percentage returns between two dates.

---

## Features

- **Bulk file ingestion** — Upload AMFI-format `.txt` files; the API parses semicolon-delimited records, validates each row, skips duplicates via a database-level unique constraint on `(scheme_code, nav_date)`, and auto-deletes the file after processing.
- **Latest NAV** — Query the most recent NAV for any fund by scheme code.
- **Historical NAV** — Retrieve NAV values across a date range, returned as a date-sorted map.
- **Percentage returns** — Calculate the return percentage between any two dates for a given fund.
- **Duplicate-safe ingestion** — Uses `existsBySchemeCodeAndNavDate` before each save to prevent re-insertion of existing records.
- **Clean layered architecture** — Controller → Service → Repository pattern with constructor injection throughout.

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- MySQL
- Maven

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/nav/bulk` | Upload an AMFI `.txt` file for bulk ingestion |
| `GET` | `/funds/{fundCode}/latest-nav` | Get the latest NAV for a fund |
| `GET` | `/funds/{fundCode}/history?fromDate=&toDate=` | Get NAV history over a date range |
| `GET` | `/funds/{fundCode}/returns?beforeDate=&afterDate=` | Get percentage returns between two dates |

Date format: `YYYY-MM-DD`

---

## Setup

### Prerequisites
- Java 21+
- MySQL 8+
- Maven

### Configuration

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/navtracker
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Run

```bash
mvn spring-boot:run
```

---

## Project Structure

```
src/main/java/com/divyesh/panchasara/NavTracker/
├── controller/
│   ├── FileController.java       # Handles file upload
│   └── NavController.java        # Handles NAV queries
├── service/
│   ├── FileService.java
│   ├── FileServiceImpl.java      # Parses file, validates rows, saves to DB
│   ├── NavService.java
│   └── NavServiceImpl.java       # Business logic for NAV queries
├── repository/
│   └── FundRepository.java       # JPA queries (latest, history, by date)
├── entity/
│   └── FundEntity.java           # DB entity with UUID PK and unique constraint
└── beans/
    ├── ResponseFund.java
    ├── ResponseFundHistory.java
    └── ResponseFundReturns.java
```

---
