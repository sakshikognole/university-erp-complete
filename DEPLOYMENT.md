# Complete Deployment Guide
# University ERP — GitHub + Render (all three services)

All three services — Node backend, Spring Boot backend, and React frontend —
are hosted on Render. No Vercel needed.

All credentials are pre-filled. Only replace YOUR_USERNAME with your GitHub username.

---

## Real Credentials (already wired into the project)

| What | Value |
|---|---|
| MongoDB Atlas URI | `mongodb+srv://khushbubaraskar2608_db_user:jFKahNQKYyDnXJn4@cluster0.36paoeu.mongodb.net/university-erp?retryWrites=true&w=majority&appName=Cluster0` |
| Database name | `university-erp` |
| JWT Secret | `university_erp_secret_key` |
| JWT Expiry | `7d` |

---

## What You Will Deploy

| Service | Type on Render | Port |
|---|---|---|
| Node backend (auth, super-admin, staff, students, venues) | Web Service (Node) | 5000 |
| Spring Boot backend (books, clubs, sports, certificates) | Web Service (Docker) | 8080 |
| React frontend (all pages) | Static Site | — |

---

## Prerequisites

```powershell
git --version
node --version
```

Both must return a version number.

---

## PART 1 — GitHub (Push Your Code)

You need three separate GitHub repositories — one per service.

### Step 1.1 — Create three empty repos on GitHub

Go to https://github.com → click **+** → **New repository** (do this 3 times):

| Repository name | Visibility |
|---|---|
| `university-erp-node` | Public |
| `university-erp-spring` | Public |
| `university-erp-frontend` | Public |

Leave "Add README", "Add .gitignore", "Choose license" all **unchecked** for all three.

For the password when pushing, use a Personal Access Token:
- Go to https://github.com/settings/tokens → **Generate new token (classic)**
- Check the **repo** scope → Generate → copy the token → use it as your password

---

### Step 1.2 — Push Node Backend

```powershell
cd "c:\Users\HP\OneDrive\Desktop\university\University-ERP-Complete\backend-node-express-mongo"
git init
git add .
git commit -m "Initial commit: University ERP Node backend"
git remote add origin https://github.com/YOUR_USERNAME/university-erp-node.git
git branch -M main
git push -u origin main
```

---

### Step 1.3 — Push Spring Boot Backend

```powershell
cd "c:\Users\HP\OneDrive\Desktop\university\University-ERP-Complete\backend-springboot"
git init
git add .
git commit -m "Initial commit: University ERP Spring Boot backend"
git remote add origin https://github.com/YOUR_USERNAME/university-erp-spring.git
git branch -M main
git push -u origin main
```

---

### Step 1.4 — Push Frontend

```powershell
cd "c:\Users\HP\OneDrive\Desktop\university\University-ERP-Complete\frontend-react"
git init
git add .
git commit -m "Initial commit: University ERP React frontend"
git remote add origin https://github.com/YOUR_USERNAME/university-erp-frontend.git
git branch -M main
git push -u origin main
```

---

## PART 2 — Render: Deploy Node Backend

### Step 2.1 — Create Render account

Go to https://render.com → **Get Started for Free** → **Sign up with GitHub** → Authorize Render.

---

### Step 2.2 — Create the Node web service

1. Render dashboard → **New +** → **Web Service**
2. Find **university-erp-node** → click **Connect**
3. Fill in:

| Field | Value |
|---|---|
| Name | `university-erp-node` |
| Region | `Singapore` |
| Branch | `main` |
| Root Directory | *(leave empty)* |
| Runtime | `Node` |
| Build Command | `npm install` |
| Start Command | `npm start` |
| Instance Type | `Free` |

4. Add these **Environment Variables**:

| Key | Value |
|---|---|
| `NODE_ENV` | `production` |
| `PORT` | `5000` |
| `MONGO_URI` | `mongodb+srv://khushbubaraskar2608_db_user:jFKahNQKYyDnXJn4@cluster0.36paoeu.mongodb.net/university-erp?retryWrites=true&w=majority&appName=Cluster0` |
| `JWT_SECRET` | `university_erp_secret_key` |
| `JWT_EXPIRES_IN` | `7d` |
| `FRONTEND_URL` | *(leave blank for now — fill after Step 4)* |

5. Click **Create Web Service**

---

### Step 2.3 — Wait and confirm

Logs tab — wait for:
```
Server is running on port 5000
Connected to MongoDB
```

Copy the URL at the top, e.g.: `https://university-erp-node.onrender.com`
**Save it — needed in Step 4.**

---

### Step 2.4 — Seed the database

Run this once locally after the Node backend is live:

```powershell
cd "c:\Users\HP\OneDrive\Desktop\university\University-ERP-Complete\backend-node-express-mongo"
npm install
node scripts/seed.js
```

Expected:
```
Connected to MongoDB for seeding...
Default system and staff roles seeded in MongoDB.
Sample users seeded successfully.
```

Default login accounts:

| Role | Email | Password |
|---|---|---|
| Super Admin | superadmin@university.edu | Admin@123 |
| Sub Admin | subadmin@university.edu | SubAdmin@123 |
| Faculty | teacher@university.edu | Teacher@123 |
| Student (PRN) | PRN2026001 | Student@123 |

---

## PART 3 — Render: Deploy Spring Boot Backend

### Step 3.1 — Fix gradlew permissions

```powershell
cd "c:\Users\HP\OneDrive\Desktop\university\University-ERP-Complete\backend-springboot"
git update-index --chmod=+x gradlew
git commit -m "Fix gradlew execute permission"
git push origin main
```

---

### Step 3.2 — Create the Spring Boot web service

1. Render dashboard → **New +** → **Web Service**
2. Find **university-erp-spring** → click **Connect**
3. Fill in:

| Field | Value |
|---|---|
| Name | `university-erp-spring` |
| Region | `Singapore` |
| Branch | `main` |
| Root Directory | *(leave empty)* |
| Runtime | `Docker` |
| Instance Type | `Free` |

4. Add **Environment Variables**:

| Key | Value |
|---|---|
| `SPRING_DATA_MONGODB_URI` | `mongodb+srv://khushbubaraskar2608_db_user:jFKahNQKYyDnXJn4@cluster0.36paoeu.mongodb.net/university-erp?retryWrites=true&w=majority&appName=Cluster0` |
| `SPRING_DATA_MONGODB_DATABASE` | `university-erp` |
| `SERVER_PORT` | `8080` |

5. Click **Create Web Service**

---

### Step 3.3 — Wait and confirm

Docker build + Gradle takes **8–12 minutes**. Wait for:
```
Started DemoApplication in X.XXX seconds
```

Copy the URL, e.g.: `https://university-erp-spring.onrender.com`
**Save it — needed in Step 4.**

---

## PART 4 — Render: Deploy Frontend as Static Site

The frontend is a **Static Site** on Render — Render builds it with
`npm run build` and serves the `dist/` folder. No server needed.

### Step 4.1 — Create the Static Site

1. Render dashboard → **New +** → **Static Site**
2. Find **university-erp-frontend** → click **Connect**
3. Fill in:

| Field | Value |
|---|---|
| Name | `university-erp-frontend` |
| Region | `Singapore` |
| Branch | `main` |
| Root Directory | *(leave empty)* |
| Build Command | `npm install && npm run build` |
| Publish Directory | `dist` |

4. Add these **Environment Variables** — use the real Render URLs from Steps 2 and 3:

| Key | Value |
|---|---|
| `VITE_NODE_API_URL` | `https://university-erp-node.onrender.com` *(your actual Node URL)* |
| `VITE_SPRING_API_URL` | `https://university-erp-spring.onrender.com` *(your actual Spring URL)* |

> These are **build-time** variables. Vite bakes them into the `dist/` bundle
> at build time, so the browser knows the exact backend URLs.

5. Click **Create Static Site**

Render runs `npm install && npm run build` (~2 minutes). When done you get:
`https://university-erp-frontend.onrender.com`

---

### Step 4.2 — Configure SPA routing

Render Static Sites need a rewrite rule so React Router works on direct URL
access (e.g. refreshing `/dashboard` doesn't 404).

This is **already handled** by `frontend-react/render.yaml` which was pushed
with your code. Render reads it automatically.

If you ever need to add it manually:
1. Render dashboard → your static site → **Redirects/Rewrites** tab
2. Add: Source `/*` → Destination `/index.html` → Action `Rewrite`

---

### Step 4.3 — Update FRONTEND_URL on the Node backend

1. Render dashboard → open **university-erp-node**
2. **Environment** tab → find `FRONTEND_URL`
3. Set it to your frontend URL, e.g. `https://university-erp-frontend.onrender.com`
4. Click **Save Changes** — Render restarts automatically

---

## PART 5 — Verify Everything Works

Open the frontend URL in your browser:

```
[ ] Login page loads at /login
[ ] Admin login works
      Email:    superadmin@university.edu
      Password: Admin@123
[ ] Super Admin Dashboard loads with stats
[ ] Departments page loads data
[ ] Students page loads data
[ ] Staff page loads data
[ ] Venues page loads data
[ ] Books page loads data (Spring Boot)
[ ] Clubs page loads data (Spring Boot)
[ ] Sports page loads data (Spring Boot)
[ ] Certificates — Select Student loads (Spring Boot)
[ ] Student login works
      PRN:      PRN2026001
      Password: Student@123
[ ] Forgot Password page loads
[ ] Logout redirects to /login
```

---

## Updating Code After Initial Deploy

Every `git push` to main auto-triggers a redeploy on Render.

### Update Node backend
```powershell
cd "c:\Users\HP\OneDrive\Desktop\university\University-ERP-Complete\backend-node-express-mongo"
git add .
git commit -m "your change"
git push origin main
```
Render redeploys in ~2 minutes.

### Update Spring Boot backend
```powershell
cd "c:\Users\HP\OneDrive\Desktop\university\University-ERP-Complete\backend-springboot"
git add .
git commit -m "your change"
git push origin main
```
Docker rebuild takes ~8–12 minutes.

### Update Frontend
```powershell
cd "c:\Users\HP\OneDrive\Desktop\university\University-ERP-Complete\frontend-react"
git add .
git commit -m "your change"
git push origin main
```
Render rebuilds static site in ~2 minutes.

---

## Troubleshooting

### CORS error in browser console after deploy

The Node backend needs to know the frontend URL to allow requests.
Make sure `FRONTEND_URL` in the Node service env vars is set to the
exact Render frontend URL (Step 4.3).

If the Node `server.js` has `cors()` with no options, all origins are
already allowed — this is fine for development. For production you can
restrict it to your frontend URL.

### API calls return 404 after deploy

The `VITE_NODE_API_URL` or `VITE_SPRING_API_URL` env vars were wrong at
build time. Fix them in Render → Static Site → Environment → then
**Manual Deploy** to rebuild with the correct values.

### Login gives "Network Error"

The Node backend URL is wrong or the Node service is still spinning up
(cold start on free tier takes 30–60 seconds on first request).
Wait 60 seconds and try again.

### Spring Boot cold start

Free tier services spin down after 15 minutes of inactivity. First
request after inactivity takes 30–60 seconds. Subsequent requests are fast.

### "Permission denied" for gradlew

```powershell
cd "c:\Users\HP\OneDrive\Desktop\university\University-ERP-Complete\backend-springboot"
git update-index --chmod=+x gradlew
git commit -m "Fix gradlew permissions"
git push origin main
```

### Blank white screen on frontend

Open browser DevTools → Console. Usually a wrong API URL baked into the
build. Fix env vars on Render → Manual Deploy → rebuild.

---

## Quick Reference

```
MongoDB URI:
  mongodb+srv://khushbubaraskar2608_db_user:jFKahNQKYyDnXJn4@cluster0.36paoeu.mongodb.net/university-erp?retryWrites=true&w=majority&appName=Cluster0

GitHub repos:
  Node:     https://github.com/YOUR_USERNAME/university-erp-node
  Spring:   https://github.com/YOUR_USERNAME/university-erp-spring
  Frontend: https://github.com/YOUR_USERNAME/university-erp-frontend

Render URLs (fill in after deploy):
  Node backend:    https://________________________________.onrender.com
  Spring backend:  https://________________________________.onrender.com
  Frontend:        https://________________________________.onrender.com

Login credentials:
  superadmin@university.edu  /  Admin@123     (Super Admin)
  subadmin@university.edu    /  SubAdmin@123  (Sub Admin)
  teacher@university.edu     /  Teacher@123   (Faculty)
  PRN2026001                 /  Student@123   (Student)
```
