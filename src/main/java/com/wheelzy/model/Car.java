package com.wheelzy.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    private String variant;

    @Column(name = "manufacture_year")
    private int year;

    private String color;

    @Column(nullable = false, unique = true)
    private String registrationNumber; // RC Number

    @Column(nullable = false)
    private String pucNumber;

    private String pucExpiry;
    private String rcExpiry;
    private String insuranceNumber;
    private String insuranceExpiry;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    private TransmissionType transmission;

    private int seats;
    private String engineCC;
    private double mileage; // kmpl

    @Column(nullable = false)
    private BigDecimal pricePerDay;

    private String location; // Area in Hyderabad
    private String latitude;
    private String longitude;

    private String carImage;
    private String description;

    private boolean available = true;
    private boolean approved = true;

    private LocalDateTime listedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    public enum FuelType { PETROL, DIESEL, ELECTRIC, CNG, HYBRID }
    public enum TransmissionType { MANUAL, AUTOMATIC }

    // Constructors
    public Car() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getVariant() { return variant; }
    public void setVariant(String variant) { this.variant = variant; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getPucNumber() { return pucNumber; }
    public void setPucNumber(String pucNumber) { this.pucNumber = pucNumber; }
    public String getPucExpiry() { return pucExpiry; }
    public void setPucExpiry(String pucExpiry) { this.pucExpiry = pucExpiry; }
    public String getRcExpiry() { return rcExpiry; }
    public void setRcExpiry(String rcExpiry) { this.rcExpiry = rcExpiry; }
    public String getInsuranceNumber() { return insuranceNumber; }
    public void setInsuranceNumber(String insuranceNumber) { this.insuranceNumber = insuranceNumber; }
    public String getInsuranceExpiry() { return insuranceExpiry; }
    public void setInsuranceExpiry(String insuranceExpiry) { this.insuranceExpiry = insuranceExpiry; }
    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }
    public TransmissionType getTransmission() { return transmission; }
    public void setTransmission(TransmissionType transmission) { this.transmission = transmission; }
    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }
    public String getEngineCC() { return engineCC; }
    public void setEngineCC(String engineCC) { this.engineCC = engineCC; }
    public double getMileage() { return mileage; }
    public void setMileage(double mileage) { this.mileage = mileage; }
    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }
    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }
    public String getCarImage() { return carImage; }
    public void setCarImage(String carImage) { this.carImage = carImage; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public LocalDateTime getListedAt() { return listedAt; }
    public void setListedAt(LocalDateTime listedAt) { this.listedAt = listedAt; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public String getDisplayName() {
        return brand + " " + model + " (" + year + ")";
    }
}
