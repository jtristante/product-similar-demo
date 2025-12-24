# Product Similar Demo

## Overview

This repository contains **`product-similar-demo`**, a technical playground project whose goal is to **design, implement and compare two backend solutions** for the same business problem using:

* **Java 17** (classic concurrency: `CompletableFuture` + dedicated executors)
* **Java 25** (modern concurrency: Virtual Threads + Structured Concurrency)

The project focuses on **performance, resilience and architectural clarity**, rather than functional complexity.

---

## Business Context

We want to offer a new feature to our customers showing **similar products** to the one they are currently viewing.

To achieve this, frontend applications agreed with the backend team on a new REST API operation that returns the **product details of similar products** for a given product.

📄 **API Contract**
The agreed REST API contract can be found here:
👉 [`similarProducts.yaml`](./similarProducts.yaml)

---

## Existing APIs

The new endpoint is built by orchestrating two existing backend APIs:

1. **Similar Product IDs API**
   Returns a list of product IDs similar to a given product.

2. **Product Detail API**
   Returns the detailed information of a product given its ID.

📄 **Existing APIs documentation**
👉 [`existingApis.yaml`](./existingApis.yaml)

---

## Target API

The application exposes the following endpoint:

```
GET /product/{productId}/similar
```

* Runs on **port 5000**
* Returns a list of product details
* Limits the number of similar products (no pagination)
* Tolerates partial failures (some products may fail)

---

## Architectural Challenge

The main technical challenge is the classic **N+1 calls problem**:

1 call → get similar product IDs
N calls → get product detail for each ID

This raises important concerns:

* Latency
* Thread exhaustion
* Failure propagation
* Overload of downstream services

This repository explores **two different implementation strategies** for solving this problem.

---

## Java 17 Solution

### Approach

### Characteristics

---

## Java 25 Solution

### Approach

### Characteristics


---

## Comparison Summary

| Aspect | Java 17 | Java 25 |
| -- |---------|---------|
|    |         |         |

---

## Project Structure

```
product-similar-demo/
├── product-api/                 # Spring Boot API
│   ├── src/main/java
│   ├── src/test/java
│   └── README.md                # How to run API with java17/java25 profiles
│
├── api-simulator/               # API mocks & benchmark tools
│   ├── docker
│   ├── scripts
│   └── README.md                # How to run simulators and load tests
│
├── similarProducts.yaml
├── existingApis.yaml
└── README.md                    # This document
```

---

## Request Flow

```
┌──────────────────────────┐
│       HTTP Client        │
│ GET /product/123/similar │
└─────────────┬────────────┘
              │
              ▼
┌──────────────────────────┐
│  Controller (Spring)     │
│ - Injects ProductService │
│   based on active profile│
└─────────────┬────────────┘
              │
              ▼
┌────────────────────────────────────────────────────┐
│   ProductService                                   │
│ - Java17: CompletableFuture + Executor             │
│ - Java21: Virtual Threads + Structured Concurrency │
│ - Calls ProductClient for IDs and details          │
└─────────────┬──────────────────────────────────────┘
              │
              ▼
┌─────────────────────────────────┐
│     ProductClient               │
│ - Makes HTTP calls to           │
│   simulator (`api-simulator`)   │
└─────────────┬───────────────────┘
              │
              ▼
┌───────────────────────┐
│     Simulated A       │
│ - /similar-ids        │
│ - /product/{id}       │
└─────────────┬─────────┘
              │
              ▼
┌──────────────────────────┐
│   Final JSON Product     │
│   returned to client     │
└──────────────────────────┘


```

* The **same flow** is used in both Java 17 and Java 25
* The **only difference** is how concurrency is implemented internally

---

## Profiles and Execution

The application is started using **Spring Profiles**:

```bash
# Java 17 implementation
./mvnw spring-boot:run -Dspring-boot.run.profiles=java17
```
```bash
# Java 25 implementation
./mvnw spring-boot:run -Dspring-boot.run.profiles=java25
```

Only **one implementation is active at a time**, ensuring clean benchmarks and comparisons.

---

## Benchmark & Resilience Testing

This project integrates code from
👉 [https://github.com/dalogax/backendDevTest](https://github.com/dalogax/backendDevTest)

* API simulators
* Latency injection
* Failure scenarios
* Load and stress testing

📜 **License note**
This project integrates `backendDevTest` code (Apache 2.0).
Any modifications are licensed under this project’s license.

---

## Goals of This Repository

* Compare **classic vs modern Java concurrency**
* Provide a realistic backend orchestration example
* Serve as a reference for architectural discussions
* Enable experimentation with resilience and performance

---

## Final Notes

This is **not a production-ready system**, but a **learning and experimentation environment** designed to explore how modern Java changes backend architecture decisions.

---

Happy benchmarking 🚀
