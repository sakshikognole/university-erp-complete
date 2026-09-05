# University ERP — Complete Bundle

This project bundles two separate workstreams into one deployable application.

| Feature | Owner | Backend |
|---|---|---|
| Login, Forgot Password | Friend | Node / Express |
| Super Admin Dashboard | Friend | Node / Express |
| Departments | Friend | Node / Express |
| Staff Management | Friend | Node / Express |
| Students Management | Friend | Node / Express |
| Venue Management | Friend | Node / Express |
| Book Management | You | Spring Boot |
| Club Management | You | Spring Boot |
| Sport Management | You | Spring Boot |
| Student Certificate | You | Spring Boot |

---

## Project Structure

```
University-ERP-Complete/
├── backend-node-express-mongo/   ← Auth, Super Admin, Staff, Students, Venues
│   ├── controllers/
│   ├── middleware/
│   ├── models/
│   ├── routes/
│   ├── scripts/seed.js
│   ├── server.js
│   ├── package.json
│   ├── Dockerfile
│   └── render.yaml               ← Render deployment config
│
├── backend-springboot/           ← Books, Clubs, Sports, Certificates
│   ├── src/main/java/com/example/demo/
│   │   ├── book/
│   │   ├── club/
│   │   ├── sport/
│   │   ├── event/
│   │   └── student/
│   ├── build.gradle              ← Spring Boot 3.2 + Lombok + iText PDF
│   ├── Dockerfile
│   └── render.yaml               ← Render deployment config
│
├── frontend-react/               ← React 19 + Vite, all pages merged
│   ├── src/
│   │   ├── App.jsx               ← All routes (auth + both feature sets)
│   │   ├── config/navigationConfig.js  ← Role-based nav (all 14 items)
│   │   ├── context/AuthContext.jsx
│   │   ├── components/
│   │   ├── pages/                ← 33 pages total
│   │   ├── services/
│   │   └── utils/
│   ├── package.json              ← includes axios
│   ├── vite.config.js            ← proxy to both backends
│   └── vercel.json               ← Vercel deployment config
│
├── .env.example                  ← Copy to .env and fill in secrets
└── .gitignore
```

---

## Local Development

### Prerequisites
- Node.js 18+
- Java 17
- MongoDB (local or Atlas)

### 1. Set up environment

```bash
cp .env.example .env
# Edit .env and fill in MONGO_URI, JWT_SECRET, etc.
```

### 2. Run Node backend

```bash
cd backend-node-express-mongo
npm install
npm run seed        # seed default users and roles (run once)
npm run dev         # starts on http://localhost:5000
```

Default seeded accounts:

| Role | Email | Password |
|---|---|---|
| Super Admin | superadmin@university.edu | Admin@123 |
| Sub Admin | subadmin@university.edu | SubAdmin@123 |
| Faculty | teacher@university.edu | Teacher@123 |
| Student (PRN login) | PRN2026001 | Student@123 |

### 3. Run Spring Boot backend

```bash
cd backend-springboot
./gradlew bootRun   # starts on http://localhost:8080
```

### 4. Run Frontend

```bash
cd frontend-react
npm install
npm run dev         # starts on http://localhost:5173
```

The Vite proxy automatically routes:
- `/api/*` → Spring Boot on `:8080` (books, clubs, sports, certificates)
- `/node-api/*` → Node on `:5000` (auth, super-admin, venues)

---

## Deployment

### Step 1 — Set up MongoDB Atlas

1. Create a free cluster at [mongodb.com/atlas](https://www.mongodb.com/atlas)
2. Create a database user and whitelist `0.0.0.0/0` for Render
3. Copy the connection string — you will need it for both backends

---

### Step 2 — Deploy Node Backend to Render

1. Push the `backend-node-express-mongo/` folder to its own GitHub repo (or a monorepo)
2. Go to [render.com](https://render.com) → New → Web Service
3. Connect the repo and set:
   - **Root Directory:** `backend-node-express-mongo`
   - **Build Command:** `npm install`
   - **Start Command:** `npm start`
   - **Environment:** Node
4. Add these environment variables in the Render dashboard:

| Key | Value |
|---|---|
| `NODE_ENV` | `production` |
| `PORT` | `5000` |
| `MONGO_URI` | your MongoDB Atlas URI |
| `JWT_SECRET` | a strong random string (32+ chars) |
| `JWT_EXPIRES_IN` | `7d` |
| `FRONTEND_URL` | your Vercel URL (add after Step 4) |

5. Deploy. Note down the URL, e.g. `https://university-erp-node-api.onrender.com`

---

### Step 3 — Deploy Spring Boot Backend to Render

1. Push the `backend-springboot/` folder to its own GitHub repo (or the same monorepo)
2. Go to Render → New → Web Service
3. Connect the repo and set:
   - **Root Directory:** `backend-springboot`
   - **Environment:** Docker (Render auto-detects the Dockerfile)
4. Add these environment variables:

| Key | Value |
|---|---|
| `SPRING_DATA_MONGODB_URI` | your MongoDB Atlas URI |
| `SPRING_DATA_MONGODB_DATABASE` | `university-erp` |
| `SERVER_PORT` | `8080` |

5. Deploy. Note down the URL, e.g. `https://university-erp-spring-api.onrender.com`

---

### Step 4 — Deploy Frontend to Vercel

1. Push the `frontend-react/` folder to its own GitHub repo (or the same monorepo)
2. Go to [vercel.com](https://vercel.com) → New Project → Import the repo
3. Set **Root Directory** to `frontend-react`
4. Framework Preset: **Vite**
5. Add these environment variables in the Vercel dashboard:

| Key | Value |
|---|---|
| `VITE_NODE_API_URL` | `https://university-erp-node-api.onrender.com` |
| `VITE_SPRING_API_URL` | `https://university-erp-spring-api.onrender.com` |

6. Deploy. Vercel will run `npm run build` automatically.
7. Copy the Vercel URL and go back to Render → Node backend → update `FRONTEND_URL` to the Vercel URL.

---

## Environment Variables Summary

### Node Backend (Render)
| Variable | Description |
|---|---|
| `MONGO_URI` | MongoDB Atlas connection string |
| `JWT_SECRET` | Secret key for signing JWT tokens |
| `JWT_EXPIRES_IN` | Token expiry, e.g. `7d` |
| `FRONTEND_URL` | Vercel frontend URL (for CORS) |
| `PORT` | `5000` |

### Spring Boot Backend (Render)
| Variable | Description |
|---|---|
| `SPRING_DATA_MONGODB_URI` | MongoDB Atlas connection string |
| `SPRING_DATA_MONGODB_DATABASE` | Database name: `university-erp` |
| `SERVER_PORT` | `8080` |

### Frontend (Vercel)
| Variable | Description |
|---|---|
| `VITE_NODE_API_URL` | Render URL of the Node backend |
| `VITE_SPRING_API_URL` | Render URL of the Spring Boot backend |

---

## API Routes Reference

### Node Backend (`/api/auth`, `/api/super-admin`, `/api/venues`)

| Method | Route | Description |
|---|---|---|
| POST | `/api/auth/admin-login` | Admin login |
| POST | `/api/auth/student-login` | Student login by PRN |
| POST | `/api/auth/send-otp` | Send OTP for password reset |
| POST | `/api/auth/reset-password` | Reset password with OTP |
| GET | `/api/super-admin/stats` | Dashboard stats |
| GET/POST | `/api/super-admin/users` | User directory |
| GET/POST/PUT/DELETE | `/api/super-admin/departments` | Departments CRUD |
| GET/POST/PUT | `/api/super-admin/students` | Students CRUD + bulk upload |
| GET/POST/PUT | `/api/super-admin/staff` | Staff CRUD |
| GET/POST/PUT/DELETE | `/api/super-admin/venues` | Venues CRUD + bulk upload |
| GET/POST/PUT/DELETE | `/api/super-admin/roles` | Roles management |
| GET | `/api/super-admin/audit-logs` | Audit logs |

### Spring Boot Backend (`/api/books`, `/api/clubs`, `/api/sports`, `/api/students`, etc.)

| Module | Base Path | Description |
|---|---|---|
| Books | `/api/books` | Book catalogue CRUD + pagination |
| Clubs | `/api/clubs` | Club management CRUD |
| Sports | `/api/sports` | Sports management CRUD |
| Events | `/api/events` | Event booking CRUD |
| Students (cert) | `/api/students` | Student lookup for certificates |
| Documents | `/api/documents` | Document type management |
| Handouts | `/api/handouts` | Handout generation |
| Letterheads | `/api/letterheads` | Letterhead editor |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React 19, Vite, React Router v7, Axios, Lucide React |
| Node Backend | Node.js 18, Express 5, Mongoose 9, JWT, bcryptjs |
| Spring Boot Backend | Spring Boot 3.2, Spring Data MongoDB, Lombok, iText PDF |
| Database | MongoDB (shared Atlas cluster, separate collections) |
| Frontend Hosting | Vercel |
| Backend Hosting | Render (free tier) |
#   U n i v e r s i t y - E R P -  
 