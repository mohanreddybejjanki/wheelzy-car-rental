package com.wheelzy.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id", nullable = false)
    private User renter;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private int totalDays;

    @Column(nullable = false)
    private BigDecimal pricePerDay;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    private String pickupLocation;
    private String dropLocation;

    private String renterNote;
    private String ownerNote;

    private LocalDateTime bookedAt = LocalDateTime.now();
    private LocalDateTime approvedAt;

    private String bookingReference;

    // Notification flags
    private boolean renterNotified = false;
    private boolean ownerNotified = false;

    public enum BookingStatus {
        PENDING, APPROVED, REJECTED, CANCELLED, COMPLETED
    }

    // Constructors
    public Booking() {}

    // Calculate total days and amount before persist
    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        if (startDate != null && endDate != null) {
            this.totalDays = (int) ChronoUnit.DAYS.between(startDate, endDate);
            if (this.totalDays < 1) this.totalDays = 1;
        }
        if (pricePerDay != null && totalDays > 0) {
            this.totalAmount = pricePerDay.multiply(BigDecimal.valueOf(totalDays));
        }
        if (bookingReference == null) {
            this.bookingReference = "WR" + System.currentTimeMillis();
        }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }
    public User getRenter() { return renter; }
    public void setRenter(User renter) { this.renter = renter; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }
    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }
    public String getRenterNote() { return renterNote; }
    public void setRenterNote(String renterNote) { this.renterNote = renterNote; }
    public String getOwnerNote() { return ownerNote; }
    public void setOwnerNote(String ownerNote) { this.ownerNote = ownerNote; }
    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
    public boolean isRenterNotified() { return renterNotified; }
    public void setRenterNotified(boolean renterNotified) { this.renterNotified = renterNotified; }
    public boolean isOwnerNotified() { return ownerNotified; }
    public void setOwnerNotified(boolean ownerNotified) { this.ownerNotified = ownerNotified; }
}
