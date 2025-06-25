# 💼 Java Developer Internship Tasks

## 🧪 Task 1: Backend - Identity Reconciliation

### ✅ Objective
Design and build a web service to reconcile contact identities (email/phone) across multiple entries, even with partial or varying data. Inspired by Doc's anonymous behavior on Zamazon.com, this service links all matching contact entries through a smart linking logic.

### 🛠️ Tech Stack
- Language:  Java 
- Framework: Spring Boot 
- Database: MySQL

### 🧩 Features
- POST `/identify` endpoint to link or create contacts
- Maintains primary and secondary contact logic
- Handles duplicate and partial data smartly
- Supports contact merging with linkedId and linkPrecedence logic

### 📦 JSON Request Example
```json
{
  "email": "doc@example.com",
  "phoneNumber": "9876543210"
}

