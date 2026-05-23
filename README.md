# 🚗 Wheelzy Rentals — Peer-to-Peer Car Rental | Hyderabad

A full-stack Spring Boot + Thymeleaf peer-to-peer car rental system.

---

## 🛠️ Tech Stack
- **Backend**: Java 17 + Spring Boot 3.2
- **Frontend**: Thymeleaf + HTML5 + CSS3 (Light Blue & White theme)
- **Database**: MySQL
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
- No JWT — authentication uses HTTP session + simple `.equals()` password check
- Maps use free OpenStreetMap via Leaflet.js (no API key needed)
