// Certificate Management LMS - API & Client Controller
// Developer: K.Deekshitha (24EG107E24) AIML-E

// Paste your deployed backend URL here once deployed (e.g. "https://cert-lms-backend.onrender.com/api")
const CLOUD_BACKEND_URL = "";

const BACKEND_URL = localStorage.getItem("lms_backend_url") || CLOUD_BACKEND_URL || "http://localhost:8080/api";
const IS_REMOTE_HOST = window.location.hostname !== "localhost" && window.location.hostname !== "127.0.0.1";

// Default Mock Data Store (Used when backend is unreachable or deployed on Netlify)
function initLocalMockStore() {
    if (!localStorage.getItem("mock_initialized")) {
        const defaultUsers = [
            { id: 1, name: "System Admin", email: "admin@lms.com", password: "admin123", role: "ADMIN", createdAt: new Date().toISOString() },
            { id: 2, name: "Dr. K. Deekshitha", email: "instructor@lms.com", password: "inst123", role: "INSTRUCTOR", createdAt: new Date().toISOString() },
            { id: 3, name: "K. Deekshitha (24EG107E24)", email: "student@lms.com", password: "stud123", role: "STUDENT", createdAt: new Date().toISOString() }
        ];
        const defaultCourses = [
            {
                id: 1,
                title: "Full Stack Artificial Intelligence & Machine Learning",
                description: "Master core AI/ML algorithms, neural networks, Spring Boot backend integrations, and modern full-stack application development.",
                category: "AIML",
                instructor: defaultUsers[1],
                createdAt: new Date().toISOString()
            }
        ];
        const defaultMaterials = [
            {
                id: 1,
                course: defaultCourses[0],
                title: "Module 1: Introduction to Supervised & Unsupervised Learning",
                contentType: "TEXT",
                content: "Supervised learning uses labeled dataset to train models to classify data or predict outcomes. Unsupervised learning handles unlabeled data to discover hidden patterns.",
                orderIndex: 1
            },
            {
                id: 2,
                course: defaultCourses[0],
                title: "Module 2: Spring Boot Microservices for AI Model Deployment",
                contentType: "TEXT",
                content: "Spring Boot offers robust RESTful APIs to serve machine learning model predictions, handle user sessions, and automate certificate generation.",
                orderIndex: 2
            }
        ];
        const defaultExams = [
            {
                id: 1,
                course: defaultCourses[0],
                title: "AI/ML & Spring Boot Certification Examination",
                passMarkPercentage: 50.0,
                durationMinutes: 20,
                totalMarks: 6,
                createdAt: new Date().toISOString()
            }
        ];
        const defaultQuestions = [
            {
                id: 1,
                examId: 1,
                questionText: "Which framework provides a structured backend for full-stack Java web applications?",
                optionA: "Django",
                optionB: "Spring Boot",
                optionC: "ExpressJS",
                optionD: "Flask",
                correctOption: "B",
                marks: 2
            },
            {
                id: 2,
                examId: 1,
                questionText: "What machine learning algorithm is commonly used for classification tasks?",
                optionA: "K-Means Clustering",
                optionB: "Linear Regression",
                optionC: "Random Forest Classifier",
                optionD: "Apriori Algorithm",
                correctOption: "C",
                marks: 2
            },
            {
                id: 3,
                examId: 1,
                questionText: "How does the system ensure certificate authenticity and prevent duplication?",
                optionA: "Manual signature only",
                optionB: "Unique Certificate ID & QR Code online verification",
                optionC: "Watermark only",
                optionD: "Paper printing",
                correctOption: "B",
                marks: 2
            }
        ];
        const defaultEnrollments = [
            {
                id: 1,
                student: defaultUsers[2],
                course: defaultCourses[0],
                status: "ENROLLED",
                progressPercentage: 0,
                enrolledAt: new Date().toISOString()
            }
        ];

        localStorage.setItem("mock_users", JSON.stringify(defaultUsers));
        localStorage.setItem("mock_courses", JSON.stringify(defaultCourses));
        localStorage.setItem("mock_materials", JSON.stringify(defaultMaterials));
        localStorage.setItem("mock_exams", JSON.stringify(defaultExams));
        localStorage.setItem("mock_questions", JSON.stringify(defaultQuestions));
        localStorage.setItem("mock_enrollments", JSON.stringify(defaultEnrollments));
        localStorage.setItem("mock_certificates", JSON.stringify([]));
        localStorage.setItem("mock_results", JSON.stringify([]));
        localStorage.setItem("mock_initialized", "true");
    }
}
initLocalMockStore();

// Local Storage Session Helpers
const Auth = {
    getUser: () => {
        const u = localStorage.getItem("lms_user");
        return u ? JSON.parse(u) : null;
    },
    setUser: (user) => {
        localStorage.setItem("lms_user", JSON.stringify(user));
    },
    logout: () => {
        localStorage.removeItem("lms_user");
        window.location.href = "login.html";
    },
    requireAuth: (allowedRoles = []) => {
        const user = Auth.getUser();
        if (!user) {
            window.location.href = "login.html";
            return null;
        }
        if (allowedRoles.length > 0 && !allowedRoles.includes(user.role)) {
            alert("Unauthorized access!");
            Auth.redirectByRole(user.role);
            return null;
        }
        return user;
    },
    redirectByRole: (role) => {
        if (role === "ADMIN") window.location.href = "admin-dashboard.html";
        else if (role === "INSTRUCTOR") window.location.href = "instructor-dashboard.html";
        else window.location.href = "student-dashboard.html";
    }
};

// Generic Fetch Wrapper (with automatic client fallback for Netlify static deployment)
async function request(endpoint, options = {}) {
    // If deployed on remote HTTPS host without custom backend or if mixed content, fallback to local store
    if (IS_REMOTE_HOST && !localStorage.getItem("lms_backend_url")) {
        return mockRequestHandler(endpoint, options);
    }

    try {
        const defaultHeaders = { "Content-Type": "application/json" };
        const config = {
            ...options,
            headers: { ...defaultHeaders, ...(options.headers || {}) }
        };
        if (options.body && typeof options.body === "object" && !(options.body instanceof FormData)) {
            config.body = JSON.stringify(options.body);
        }

        const response = await fetch(`${BACKEND_URL}${endpoint}`, config);
        if (!response.ok) {
            const errText = await response.text();
            throw new Error(errText || `HTTP Error ${response.status}`);
        }
        
        const contentType = response.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
            return await response.json();
        }
        return await response.text();
    } catch (err) {
        console.warn("Backend unavailable, using simulated offline data:", err.message);
        return mockRequestHandler(endpoint, options);
    }
}

// Client-Side Mock Handler for Netlify Deployment
function mockRequestHandler(endpoint, options = {}) {
    initLocalMockStore();
    const method = options.method || "GET";
    const body = options.body ? (typeof options.body === "string" ? JSON.parse(options.body) : options.body) : {};

    // Auth
    if (endpoint === "/auth/login") {
        const users = JSON.parse(localStorage.getItem("mock_users") || "[]");
        const found = users.find(u => u.email === body.email && u.password === body.password);
        if (found) {
            return { userId: found.id, name: found.name, email: found.email, role: found.role, message: "Login successful!" };
        }
        throw new Error("Invalid email or password!");
    }
    if (endpoint === "/auth/register") {
        const users = JSON.parse(localStorage.getItem("mock_users") || "[]");
        if (users.find(u => u.email === body.email)) throw new Error("Email already registered!");
        const newUser = { id: Date.now(), name: body.name, email: body.email, password: body.password, role: body.role || "STUDENT", createdAt: new Date().toISOString() };
        users.push(newUser);
        localStorage.setItem("mock_users", JSON.stringify(users));
        return { userId: newUser.id, name: newUser.name, email: newUser.email, role: newUser.role, message: "Registered successfully!" };
    }

    // Courses
    if (endpoint === "/courses" && method === "GET") {
        return JSON.parse(localStorage.getItem("mock_courses") || "[]");
    }
    if (endpoint.startsWith("/courses/") && endpoint.includes("/materials") && method === "GET") {
        const courseId = parseInt(endpoint.split("/")[2]);
        const mats = JSON.parse(localStorage.getItem("mock_materials") || "[]");
        return mats.filter(m => m.course.id === courseId);
    }
    if (endpoint.startsWith("/courses/") && endpoint.includes("/materials") && method === "POST") {
        const courseId = parseInt(endpoint.split("/")[2]);
        const courses = JSON.parse(localStorage.getItem("mock_courses") || "[]");
        const course = courses.find(c => c.id === courseId);
        const mats = JSON.parse(localStorage.getItem("mock_materials") || "[]");
        const newMat = { id: Date.now(), course, title: body.title, contentType: body.contentType, content: body.content, orderIndex: body.orderIndex };
        mats.push(newMat);
        localStorage.setItem("mock_materials", JSON.stringify(mats));
        return newMat;
    }
    if (endpoint.startsWith("/courses/") && endpoint.includes("/enroll") && method === "POST") {
        const urlParts = endpoint.split("?");
        const courseId = parseInt(urlParts[0].split("/")[2]);
        const params = new URLSearchParams(urlParts[1]);
        const studentId = parseInt(params.get("studentId"));

        const courses = JSON.parse(localStorage.getItem("mock_courses") || "[]");
        const users = JSON.parse(localStorage.getItem("mock_users") || "[]");
        const enrollments = JSON.parse(localStorage.getItem("mock_enrollments") || "[]");

        let existing = enrollments.find(e => e.student.id === studentId && e.course.id === courseId);
        if (!existing) {
            existing = {
                id: Date.now(),
                student: users.find(u => u.id === studentId) || { id: studentId, name: "Student" },
                course: courses.find(c => c.id === courseId),
                status: "ENROLLED",
                progressPercentage: 0,
                enrolledAt: new Date().toISOString()
            };
            enrollments.push(existing);
            localStorage.setItem("mock_enrollments", JSON.stringify(enrollments));
        }
        return existing;
    }
    if (endpoint.startsWith("/courses/student/") && endpoint.endsWith("/enrollments")) {
        const studentId = parseInt(endpoint.split("/")[3]);
        const enrollments = JSON.parse(localStorage.getItem("mock_enrollments") || "[]");
        return enrollments.filter(e => e.student.id === studentId);
    }
    if (endpoint.startsWith("/courses/instructor/")) {
        const instructorId = parseInt(endpoint.split("/")[3]);
        const courses = JSON.parse(localStorage.getItem("mock_courses") || "[]");
        return courses.filter(c => c.instructor && c.instructor.id === instructorId);
    }
    if (endpoint === "/courses" && method === "POST") {
        const courses = JSON.parse(localStorage.getItem("mock_courses") || "[]");
        const users = JSON.parse(localStorage.getItem("mock_users") || "[]");
        const instructor = users.find(u => u.id === body.instructorId);
        const newCourse = { id: Date.now(), title: body.title, description: body.description, category: body.category, instructor, createdAt: new Date().toISOString() };
        courses.push(newCourse);
        localStorage.setItem("mock_courses", JSON.stringify(courses));
        return newCourse;
    }

    // Exams
    if (endpoint.startsWith("/exams/course/")) {
        const courseId = parseInt(endpoint.split("/")[3]);
        const exams = JSON.parse(localStorage.getItem("mock_exams") || "[]");
        return exams.filter(e => e.course.id === courseId);
    }
    if (endpoint.startsWith("/exams/") && endpoint.endsWith("/questions")) {
        const examId = parseInt(endpoint.split("/")[2]);
        const questions = JSON.parse(localStorage.getItem("mock_questions") || "[]");
        return questions.filter(q => q.examId === examId);
    }
    if (endpoint.startsWith("/exams/") && !endpoint.includes("questions") && method === "GET") {
        const examId = parseInt(endpoint.split("/")[2]);
        const exams = JSON.parse(localStorage.getItem("mock_exams") || "[]");
        return exams.find(e => e.id === examId);
    }
    if (endpoint === "/exams/submit" && method === "POST") {
        const questions = JSON.parse(localStorage.getItem("mock_questions") || "[]").filter(q => q.examId === body.examId);
        const exams = JSON.parse(localStorage.getItem("mock_exams") || "[]");
        const exam = exams.find(e => e.id === body.examId);
        const users = JSON.parse(localStorage.getItem("mock_users") || "[]");
        const student = users.find(u => u.id === body.studentId);

        let score = 0;
        let total = 0;
        questions.forEach(q => {
            total += q.marks;
            const submitted = body.answers.find(a => a.questionId === q.id);
            if (submitted && submitted.selectedOption && submitted.selectedOption.toUpperCase() === q.correctOption.toUpperCase()) {
                score += q.marks;
            }
        });

        const percentage = total > 0 ? (score / total) * 100 : 0;
        const status = percentage >= (exam ? exam.passMarkPercentage : 50) ? "PASSED" : "FAILED";

        const result = {
            id: Date.now(),
            student,
            exam,
            score,
            totalMarks: total,
            percentage,
            status,
            completedAt: new Date().toISOString()
        };

        const results = JSON.parse(localStorage.getItem("mock_results") || "[]");
        results.push(result);
        localStorage.setItem("mock_results", JSON.stringify(results));

        if (status === "PASSED") {
            const certs = JSON.parse(localStorage.getItem("mock_certificates") || "[]");
            const certId = "CERT-2026-" + Math.random().toString(36).substring(2, 10).toUpperCase();
            const verifyUrl = `${window.location.origin}/verify.html?id=${certId}`;
            const qrCodeUrl = `https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=${encodeURIComponent(verifyUrl)}`;

            const cert = {
                id: Date.now(),
                certificateId: certId,
                student,
                course: exam.course,
                issueDate: new Date().toISOString().split("T")[0],
                scorePercentage: percentage,
                verificationUrl: verifyUrl,
                qrCodeBase64: qrCodeUrl
            };
            certs.push(cert);
            localStorage.setItem("mock_certificates", JSON.stringify(certs));

            // update enrollment progress to 100%
            const enrollments = JSON.parse(localStorage.getItem("mock_enrollments") || "[]");
            const en = enrollments.find(e => e.student.id === student.id && e.course.id === exam.course.id);
            if (en) {
                en.status = "COMPLETED";
                en.progressPercentage = 100;
                localStorage.setItem("mock_enrollments", JSON.stringify(enrollments));
            }
        }

        return result;
    }
    if (endpoint.startsWith("/exams/student/") && endpoint.endsWith("/results")) {
        const studentId = parseInt(endpoint.split("/")[3]);
        const results = JSON.parse(localStorage.getItem("mock_results") || "[]");
        return results.filter(r => r.student && r.student.id === studentId);
    }
    if (endpoint === "/exams" && method === "POST") {
        const exams = JSON.parse(localStorage.getItem("mock_exams") || "[]");
        const questions = JSON.parse(localStorage.getItem("mock_questions") || "[]");
        const courses = JSON.parse(localStorage.getItem("mock_courses") || "[]");
        const course = courses.find(c => c.id === parseInt(body.courseId));

        let totalMarks = 0;
        const examId = Date.now();
        if (body.questions) {
            body.questions.forEach((q, idx) => {
                totalMarks += (q.marks || 1);
                questions.push({
                    id: examId + idx + 1,
                    examId,
                    questionText: q.questionText,
                    optionA: q.optionA,
                    optionB: q.optionB,
                    optionC: q.optionC,
                    optionD: q.optionD,
                    correctOption: q.correctOption,
                    marks: q.marks || 1
                });
            });
        }

        const newExam = {
            id: examId,
            course,
            title: body.title,
            passMarkPercentage: body.passMarkPercentage || 50,
            durationMinutes: body.durationMinutes || 30,
            totalMarks,
            createdAt: new Date().toISOString()
        };
        exams.push(newExam);
        localStorage.setItem("mock_exams", JSON.stringify(exams));
        localStorage.setItem("mock_questions", JSON.stringify(questions));
        return newExam;
    }

    // Certificates
    if (endpoint.startsWith("/certificates/student/")) {
        const studentId = parseInt(endpoint.split("/")[3]);
        const certs = JSON.parse(localStorage.getItem("mock_certificates") || "[]");
        return certs.filter(c => c.student && c.student.id === studentId);
    }
    if (endpoint.startsWith("/certificates/verify/")) {
        const certId = decodeURIComponent(endpoint.split("/")[3]).trim();
        const certs = JSON.parse(localStorage.getItem("mock_certificates") || "[]");
        const found = certs.find(c => c.certificateId.toUpperCase() === certId.toUpperCase());
        if (found) return found;
        throw new Error("Certificate not found or invalid!");
    }
    if (endpoint === "/certificates" && method === "GET") {
        return JSON.parse(localStorage.getItem("mock_certificates") || "[]");
    }

    // Admin
    if (endpoint === "/admin/users") {
        return JSON.parse(localStorage.getItem("mock_users") || "[]");
    }
    if (endpoint === "/admin/stats") {
        const users = JSON.parse(localStorage.getItem("mock_users") || "[]");
        const courses = JSON.parse(localStorage.getItem("mock_courses") || "[]");
        const certs = JSON.parse(localStorage.getItem("mock_certificates") || "[]");
        return { totalUsers: users.length, totalCourses: courses.length, totalCertificates: certs.length };
    }

    return {};
}

// API Modules
const AuthAPI = {
    login: (email, password) => request("/auth/login", { method: "POST", body: { email, password } }),
    register: (name, email, password, role) => request("/auth/register", { method: "POST", body: { name, email, password, role } })
};

const CourseAPI = {
    getAll: () => request("/courses"),
    getById: (id) => request(`/courses/${id}`),
    create: (data) => request("/courses", { method: "POST", body: data }),
    getByInstructor: (instructorId) => request(`/courses/instructor/${instructorId}`),
    getMaterials: (courseId) => request(`/courses/${courseId}/materials`),
    addMaterial: (courseId, data) => request(`/courses/${courseId}/materials`, { method: "POST", body: data }),
    enroll: (courseId, studentId) => request(`/courses/${courseId}/enroll?studentId=${studentId}`, { method: "POST" }),
    getStudentEnrollments: (studentId) => request(`/courses/student/${studentId}/enrollments`)
};

const ExamAPI = {
    getByCourse: (courseId) => request(`/exams/course/${courseId}`),
    getById: (examId) => request(`/exams/${examId}`),
    getQuestions: (examId) => request(`/exams/${examId}/questions`),
    create: (data) => request("/exams", { method: "POST", body: data }),
    submit: (submissionData) => request("/exams/submit", { method: "POST", body: submissionData }),
    getStudentResults: (studentId) => request(`/exams/student/${studentId}/results`)
};

const CertAPI = {
    getStudentCertificates: (studentId) => request(`/certificates/student/${studentId}`),
    verify: (certificateId) => request(`/certificates/verify/${encodeURIComponent(certificateId)}`),
    getPdfUrl: (certificateId) => {
        if (IS_REMOTE_HOST && !localStorage.getItem("lms_backend_url")) {
            return `javascript:window.print()`;
        }
        return `${BACKEND_URL}/certificates/${encodeURIComponent(certificateId)}/pdf`;
    },
    getAll: () => request("/certificates")
};

const AdminAPI = {
    getUsers: () => request("/admin/users"),
    getStats: () => request("/admin/stats")
};

// UI Toast Notification
function showToast(message, type = "info") {
    let container = document.getElementById("toast-container");
    if (!container) {
        container = document.createElement("div");
        container.id = "toast-container";
        container.style.position = "fixed";
        container.style.top = "20px";
        container.style.right = "20px";
        container.style.zIndex = "9999";
        document.body.appendChild(container);
    }
    const toast = document.createElement("div");
    toast.className = `alert alert-${type === 'error' ? 'danger' : type}`;
    toast.style.boxShadow = "var(--shadow-lg)";
    toast.style.marginBottom = "10px";
    toast.style.minWidth = "260px";
    toast.innerHTML = message;
    container.appendChild(toast);
    setTimeout(() => {
        toast.remove();
    }, 4000);
}
