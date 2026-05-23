# 🚗 Wheelzy Rentals — Peer-to-Peer Car Rental | Hyderabad

A full-stack Spring Boot + Thymeleaf peer-to-peer car rental system inspired by ZoomCar, built for Hyderabad.

---

## 🛠️ Tech Stack
- **Backend**: Java 17 + Spring Boot 3.2
- **Frontend**: Thymeleaf + HTML5 + CSS3 (Light Blue & White theme)
- **Database**: H2 In-Memory (auto-seeded with 10 cars & 13 users)
- **Maps**: Leaflet.js (OpenStreetMap — Free, no API key needed)
- **PDF**: iText 7 (booking receipts)
- **Auth**: Simple session-based (password compared with `.equals()`, no JWT)

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps
```bash
cd wheelzy-rentals
mvn spring-boot:run
```

Then open: **http://localhost:8080**

---

## 👥 Pre-seeded Demo Accounts

### 🔑 Renters (can browse & book cars)
| Name | Email | Password |
|------|-------|----------|
| Test Renter | renter@example.com | renter123 |
| Demo User | demo@example.com | demo123 |

### 🚗 Car Owners (can manage cars & approve bookings)
| Name | Email | Password | Area |
|------|-------|----------|------|
| Rahul Sharma | rahul@example.com | rahul123 | Banjara Hills |
| Priya Reddy | priya@example.com | priya123 | Jubilee Hills |
| Kiran Kumar | kiran@example.com | kiran123 | Gachibowli |
| Sneha Patel | sneha@example.com | sneha123 | Madhapur |
| Arun Nair | arun@example.com | arun123 | Hitech City |
| Divya Menon | divya@example.com | divya123 | Kondapur |
| Vikram Singh | vikram@example.com | vikram123 | Kukatpally |
| Anita Joshi | anita@example.com | anita123 | Secunderabad |
| Ravi Teja | ravi@example.com | ravi123 | Ameerpet |
| Meena Iyer | meena@example.com | meena123 | Begumpet |

### 🔧 Admin
| Email | Password |
|-------|----------|
| admin@wheelzy.com | admin123 |

---

## 🚙 Pre-seeded Cars (10 Cars)

| # | Car | Fuel | Price/Day | Location |
|---|-----|------|-----------|----------|
| 1 | Maruti Swift VXI (2022) | Petrol | ₹1,499 | Banjara Hills |
| 2 | Hyundai Creta SX (2023) | Petrol | ₹2,499 | Jubilee Hills |
| 3 | Toyota Innova Crysta ZX (2021) | Diesel | ₹3,499 | Gachibowli |
| 4 | Tata Nexon EV Max (2023) | Electric | ₹2,799 | Madhapur |
| 5 | Honda City ZX CVT (2022) | Petrol | ₹1,999 | Hitech City |
| 6 | Kia Seltos HTX Plus (2023) | Petrol | ₹2,299 | Kondapur |
| 7 | Mahindra XUV700 AX7 (2022) | Diesel | ₹3,999 | Kukatpally |
| 8 | Maruti Baleno Alpha (2023) | Petrol | ₹1,699 | Secunderabad |
| 9 | MG Hector Sharp Pro (2023) | Petrol | ₹2,799 | Ameerpet |
| 10 | Skoda Slavia Style (2022) | Petrol | ₹2,199 | Begumpet |

---

## 🗺️ Key Features

### For Renters
- Browse 10+ verified cars with full details
- Search by brand, model, or Hyderabad area
- View car location on interactive Leaflet map
- Book car (minimum 1 day) with date picker
- Real-time price calculation before booking
- Get notified when owner approves/rejects
- Download PDF receipt for approved bookings
- Cancel pending bookings

### For Owners
- Owner dashboard with stats
- View & approve/reject booking requests
- Real-time notifications when someone books their car
- List new cars with all document details
- Toggle car availability

### For All Users
- In-app notification bell with unread count
- PDF receipt download with complete booking details (iText7)
- OpenStreetMap / Leaflet map (FREE — no API key)
- Responsive design (mobile-friendly)

---

## 📁 Project Structure

```
wheelzy-rentals/
├── src/main/java/com/wheelzy/
│   ├── WheelzyRentalsApplication.java
│   ├── config/
│   │   └── DataSeeder.java          ← Seeds 10 cars + 13 users
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── BookingController.java
│   │   ├── CarController.java
│   │   ├── HomeController.java
│   │   ├── NotificationController.java
│   │   └── OwnerController.java
│   ├── model/
│   │   ├── Booking.java
│   │   ├── Car.java
│   │   ├── Notification.java
│   │   └── User.java
│   ├── repository/
│   │   ├── BookingRepository.java
│   │   ├── CarRepository.java
│   │   ├── NotificationRepository.java
│   │   └── UserRepository.java
│   └── service/
│       ├── BookingService.java
│       ├── CarService.java
│       ├── NotificationService.java
│       ├── PdfReceiptService.java
│       └── UserService.java
├── src/main/resources/
│   ├── application.properties
│   ├── static/
│   │   ├── css/style.css
│   │   ├── js/main.js
│   │   └── images/car-placeholder.svg
│   └── templates/
│       ├── index.html               ← Homepage with map
│       ├── about.html
│       ├── notifications.html
│       ├── auth/
│       │   ├── login.html
│       │   └── register.html
│       ├── bookings/
│       │   ├── book.html
│       │   └── my-bookings.html
│       ├── cars/
│       │   ├── detail.html
│       │   └── list.html
│       ├── fragments/
│       │   ├── footer.html
│       │   ├── head.html
│       │   └── navbar.html
│       └── owner/
│           ├── add-car.html
│           ├── bookings.html
│           └── dashboard.html
└── pom.xml
```

---

## 🔄 Booking Flow

```
Renter browses cars → Selects dates → Submits booking request
    ↓
Owner gets notification → Reviews request → Approves or Rejects
    ↓
Renter gets notification → Downloads PDF receipt → Picks up car
```

---

## 📊 H2 Console (Dev Only)
URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:wheelzydb`
- Username: `sa`
- Password: (empty)

---

## ⚠️ Notes
- Database resets on each restart (H2 in-memory). Data is re-seeded automatically.
- For production, replace H2 with MySQL/PostgreSQL in `application.properties`
- No JWT — authentication uses HTTP session + simple `.equals()` password check
- Maps use free OpenStreetMap via Leaflet.js (no API key needed)
