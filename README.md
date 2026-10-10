# Certificate Management Learning Management System (LMS)

### Student Details
* **Student Name:** K. Deekshitha
* **Roll Number:** 24EG107E24
* **Department / Section:** AIML-E

---

## 📌 Abstract
The **Certificate Management Learning Management System (LMS)** is a full-stack web application designed to simplify and automate the management of online courses, examinations, student performance, and digital certificates. The system provides a centralized platform where students can register, enroll in courses, access learning materials, participate in examinations, view their results, and receive verifiable digital certificates upon satisfying course requirements.

A central feature of the system is **automated digital certificate generation** with dynamic **QR code security** and **online authenticity verification**. When a student passes the course examination, the system instantly generates an official PDF certificate with their name, course title, score, issue date, and a unique Certificate ID. Authorized verifiers (employers, academic institutions) can scan the QR code or enter the Certificate ID to validate authenticity in real time, preventing fraudulent or forged credentials.

---

## 🛠️ Technology Stack
* **Backend:** Spring Boot 3 (Java 17/21/26), Spring Data JPA, Hibernate, Maven
* **Database:** MySQL (with automatic schema creation and seed data)
* **Digital Certificate & Security:** OpenPDF (PDF document rendering), ZXing (QR Code generation)
* **Frontend:** Modern HTML5, Responsive CSS3, Modular JavaScript (Fetch API, REST client)
* **Architecture:** Decoupled Full-Stack Architecture with separate `backend` and `frontend` folders

---

## 📁 Project Structure

```
portal-lms/
│
├── backend/                                   # Spring Boot Maven Project
│   ├── pom.xml                                # Maven Dependencies (Spring Boot, JPA, MySQL, OpenPDF, ZXing)
│   └── src/main/
│       ├── java/com/lms/certsystem/
│       │   ├── CertLmsApplication.java        # Spring Boot Entry Point
│       │   ├── config/
│       │   │   └── WebConfig.java             # CORS & MVC Configuration
│       │   ├── entity/                        # JPA Entities
│       │   │   ├── User.java                  # Admin, Instructor, Student
│       │   │   ├── Course.java                # Course entity
│       │   │   ├── Material.java              # Lessons and Study Modules
│       │   │   ├── Enrollment.java            # Student Course Enrollments
│       │   │   ├── Exam.java                  # Assessments & Pass Criteria
│       │   │   ├── Question.java              # Multiple Choice Questions
│       │   │   ├── ExamResult.java            # Scores, Percentage & Status
│       │   │   └── Certificate.java           # Cert ID, Issue Date, QR Code
│       │   ├── repository/                    # Spring Data JPA Repositories
│       │   ├── service/                       # Business Logic Layer
│       │   │   ├── UserService.java
│       │   │   ├── CourseService.java
│       │   │   ├── ExamService.java
│       │   │   ├── CertificateService.java
│       │   │   └── DataInitializer.java       # Automatic DB Seeding Runner
│       │   ├── controller/                    # RESTful Endpoints
│       │   │   ├── AuthController.java        # /api/auth
│       │   │   ├── CourseController.java      # /api/courses
│       │   │   ├── ExamController.java        # /api/exams
│       │   │   ├── CertificateController.java # /api/certificates
│       │   │   └── AdminController.java       # /api/admin
│       │   ├── dto/                           # Data Transfer Objects
│       │   └── util/                          # PDF & QR Code Generators
│       │       ├── QRCodeGenerator.java       # ZXing QR Generator
│       │       └── PDFCertificateGenerator.java# OpenPDF Certificate Renderer
│       └── resources/
│           ├── application.properties         # MySQL Database Configuration
│           └── application-h2.properties      # In-Memory H2 Testing Profile
│
├── frontend/                                  # Full-Stack Frontend Web Application
│   ├── css/
│   │   └── style.css                          # Modern Responsive Styles & Certificate Layout
│   ├── js/
│   │   └── api.js                             # Backend API Connector & Session Storage
│   ├── index.html                             # Home Landing Page & Quick Verification
│   ├── login.html                             # Role-based Login with 1-click Demo Fillers
│   ├── register.html                          # Student & Instructor Registration
│   ├── student-dashboard.html                 # Student Learning, Courses & Certificate Viewer
│   ├── instructor-dashboard.html              # Instructor Course & Exam Creation Portal
│   ├── admin-dashboard.html                   # Admin Management & Issued Certs Audit
│   ├── exam.html                              # Interactive Examination Portal with Timer
│   └── verify.html                            # Public Online Certificate Authenticity Checker
│
├── schema.sql                                 # Complete MySQL Database Schema Script
└── README.md                                  # Documentation and Setup Instructions
```

---

## 🚀 How to Run the Application

### Step 1: Database Setup (MySQL)
1. Ensure your MySQL server is running (e.g., via MySQL Workbench, XAMPP, or MySQL Command Line).
2. The database `cert_lms_db` will be created automatically by Spring Boot!
3. (Optional) If you wish to manually create the tables and seed data, execute the `schema.sql` script located in the project root:
   ```sql
   mysql -u root -p < schema.sql
   ```
4. Verify your MySQL credentials in `backend/src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/cert_lms_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=root
   ```

---

### Step 2: Run the Spring Boot Backend

Open your terminal or PowerShell, navigate to the `backend` folder, and launch:

```powershell
cd c:\Users\DELL'\Desktop\portal-lms\backend
mvn spring-boot:run
```

> **Note:** If you want to run the backend in standalone in-memory mode without starting MySQL, simply add the H2 profile:
> ```powershell
> mvn spring-boot:run -Dspring-boot.run.profiles=h2
> ```

The Spring Boot backend will start on **`http://localhost:8080`**.
On first startup, the built-in `DataInitializer` automatically populates:
- 1 Admin account
- 1 Instructor account
- 1 Student account
- 1 Pre-configured Artificial Intelligence & Machine Learning course with study modules and an interactive certification exam!

---

### Step 3: Run the Frontend

You can run the frontend in any of the following simple ways:

#### Option A: Using Python's built-in HTTP server (Recommended)
Open a new terminal window:
```powershell
cd c:\Users\DELL'\Desktop\portal-lms\frontend
python -m http.server 3000
```
Then visit **`http://localhost:3000`** in your web browser.

#### Option B: Direct Browser Launch
Simply double-click or open `frontend/index.html` in any browser (Chrome, Edge, Firefox).

---

## 👥 Default Demo Credentials

You can use the 1-click login buttons on `frontend/login.html` or enter the credentials below:

| Role | Email | Password | Permissions |
|---|---|---|---|
| **Student** | `student@lms.com` | `stud123` | Enroll in courses, view lessons, take exams, view & download QR-secured certificates |
| **Instructor** | `instructor@lms.com` | `inst123` | Create courses, publish study modules, author exams & questions |
| **Administrator** | `admin@lms.com` | `admin123` | View all users, inspect all courses, audit all issued certificates & statistics |

---

## 🔄 End-to-End Workflow Demonstration

1. **Sign in as Student (`student@lms.com` / `stud123`)**:
   - Access **My Enrolled Courses** or explore the **Course Catalog**.
   - Click **Study Lessons** to read the course modules.
   - Click **Take Exam** to open the timed assessment screen (`exam.html`).
2. **Take the Assessment**:
   - Answer the multiple-choice questions.
   - Submit the exam to receive instant automatic evaluation.
3. **Earn Digital Certificate**:
   - Upon scoring above the passing threshold (50%), the system automatically creates an official Certificate entry with a unique ID (e.g. `CERT-2026-XXXXXXXX`) and an encoded QR code!
   - Click **Download Certificate PDF** to receive the high-resolution generated PDF document.
4. **Online Public Verification**:
   - Open **`verify.html`** or scan the QR code using any smartphone or camera.
   - Enter the Certificate ID to view the authentic verification record directly from the database!
