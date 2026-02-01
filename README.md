# 🧠 AI-Powered Research Assistant — Backend (Spring Boot)

This repository contains the **Spring Boot backend server** for the AI-Powered Research Assistant project.

It processes research queries, summarizes webpage content, and communicates with the Chrome Extension frontend.

---

## 🏗 Project Architecture

| Layer | Repository | Description |
|------|-------------|-------------|
| 🎯 Frontend | Chrome Extension | User interface (side panel) |
| 🧠 Backend | This Repo | AI processing & REST APIs |

Frontend repo:  
👉 `https://github.com/rekandlal/Frontend-AI-Powered-Research-Assistant-Extension`

---

## ⚙️ Technologies Used

- Java  
- Spring Boot  
- REST APIs  
- AI / NLP Integration (API-based)  

---

## 🚀 How to Run the Backend

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/YOUR-USERNAME/YOUR-BACKEND-REPO.git
cd YOUR-BACKEND-REPO
```

---

### 2️⃣ Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

Or run the main class from your IDE.

Server will start at:

```
http://localhost:8080
```

---

## 🔗 API Endpoints (Example)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/summarize | Summarizes webpage content |
| POST | /api/research | Answers research queries |

---

## 🌐 How It Connects to Extension

The Chrome Extension sends webpage text and user queries to this backend server.  
The backend processes the request using AI APIs and sends the response back.

Make sure CORS is enabled for extension requests.

---

## ❓ Troubleshooting

**Port already in use**
- Change server port in `application.properties`

**Extension not connecting**
- Ensure backend is running
- Check CORS configuration
- Verify API URLs in extension code

---

## 📌 Future Improvements

- Add authentication  
- Store research history  
- Improve AI response accuracy  
- Deploy backend to cloud  

---

💡 This backend powers the AI features of the Chrome Extension frontend.
