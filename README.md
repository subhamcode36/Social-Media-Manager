# 📱 SocialSphere - Java-Based Social Media Content Manager & Analytics 📊

Welcome to **SocialSphere**, your all-in-one content scheduling and analytics platform built entirely in **Java OOP**! Designed for **influencers**, **brands**, and **marketing pros**, this system helps you **plan smarter**, **post faster**, and **analyze deeper** — across Facebook, Instagram, Twitter, and LinkedIn.

---

## 🚀 Features at a Glance
✅ Schedule posts across multiple platforms  
✅ Track likes, shares, comments, and follower growth  
✅ Get AI-powered content & timing recommendations  
✅ Perform sentiment analysis on interactions  
✅ Monitor hashtag performance  
✅ Benchmark competitors  
✅ View stunning data visualizations (charts, graphs, heatmaps)  
✅ Secure role-based access (Admin, Content Creator, Marketing Analyst)  
✅ Real-time file-based analytics simulation  
✅ Clean object-oriented architecture

---

## 🧠 Tech Stack & OOP Principles

| Concept                     | Used In                                   |
|-----------------------------|--------------------------------------------|
| **Abstract Class**         | `User` – base class for all user roles     |
| **Interfaces**             | `MetricsProcessor`, `APIIntegration`       |
| **Hierarchical Inheritance** | `User → Admin`, `ContentCreator`, `Analyst` |
| **Multiple Inheritance**   | Class implementing multiple interfaces     |
| **Method Overloading**     | `schedulePost()` – different parameter sets|
| **Constructor Overloading**| `User(String)`, `User(String, String)`     |
| **Varargs**                | `schedulePost(String... platforms)`        |
| **Nested Class**           | `ScheduledPost` inside `PostScheduler`     |
| **Wrapper Classes**        | `Integer`, `Double` for metrics            |
| **File Handling**          | Save/load analytics, posts                 |
| **Scanner Input**          | Read user data                             |
| **Multithreading**         | `ReportGenerator` using `Runnable`         |
| **Exception Handling**     | Custom `InvalidTokenException`, `DataException` |
| **Package Structure**      | `package socialmedia;`                     |

---

## 📁 Class Overview

- 🔒 `User` *(abstract)* – base class for all users  
  - 👑 `Admin`, ✍️ `ContentCreator`, 📈 `MarketingAnalyst` (subclasses)

- 🗓 `PostScheduler` – handles post scheduling with vararg & method overloads  
  - 📌 `ScheduledPost` *(non-static nested class)*  

- 📊 `Analytics` – tracks and stores engagement metrics  
  - Implements `MetricsProcessor`

- 🧠 `AIEngine` – recommends best time to post, analyzes sentiment, hashtag ranking  
  - Uses wrapper classes

- 🌐 `SocialMediaAPIConnector` – simulates secure API connections  
  - Implements `APIIntegration`

- 📑 `ReportGenerator` – multithreaded visual report engine (ASCII charts or file export)

---

## 🔧 How to Run Locally

### 1. 📦 Compile the Project
```bash
javac socialmedia/*.java
