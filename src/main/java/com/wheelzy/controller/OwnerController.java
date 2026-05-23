package com.wheelzy.controller;

import com.wheelzy.model.Car;
import com.wheelzy.model.User;
import com.wheelzy.service.BookingService;
import com.wheelzy.service.CarService;
import com.wheelzy.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @Autowired private BookingService bookingService;
    @Autowired private CarService carService;
    @Autowired private NotificationService notificationService;

    private User getOwner(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.OWNER) return null;
        return user;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User owner = getOwner(session);
        if (owner == null) return "redirect:/login";

        model.addAttribute("user", owner);
        model.addAttribute("cars", carService.getCarsByOwner(owner));
        model.addAttribute("pendingBookings", bookingService.getPendingBookingsForOwner(owner));
        model.addAttribute("allBookings", bookingService.getBookingsForOwner(owner));
        model.addAttribute("unreadCount", notificationService.getUnreadCount(owner));
        return "owner/dashboard";
    }

    @GetMapping("/bookings")
    public String ownerBookings(Model model, HttpSession session) {
        User owner = getOwner(session);
        if (owner == null) return "redirect:/login";

        model.addAttribute("user", owner);
        model.addAttribute("bookings", bookingService.getBookingsForOwner(owner));
        model.addAttribute("unreadCount", notificationService.getUnreadCount(owner));
        return "owner/bookings";
    }

    @PostMapping("/bookings/{id}/approve")
    public String approveBooking(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        User owner = getOwner(session);
        if (owner == null) return "redirect:/login";

        try {
            bookingService.approveBooking(id, owner);
            ra.addFlashAttribute("success", "Booking approved! Renter has been notified.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/owner/bookings";
    }

    @PostMapping("/bookings/{id}/reject")
    public String rejectBooking(@PathVariable Long id,
                                @RequestParam(required = false) String reason,
                                HttpSession session, RedirectAttributes ra) {
        User owner = getOwner(session);
        if (owner == null) return "redirect:/login";

        try {
            bookingService.rejectBooking(id, owner, reason);
            ra.addFlashAttribute("success", "Booking rejected. Renter has been notified.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/owner/bookings";
    }

    @GetMapping("/cars/add")
    public String addCarForm(Model model, HttpSession session) {
        User owner = getOwner(session);
        if (owner == null) return "redirect:/login";
        model.addAttribute("user", owner);
        model.addAttribute("unreadCount", notificationService.getUnreadCount(owner));
        return "owner/add-car";
    }

    @PostMapping("/cars/add")
    public String addCar(@RequestParam String brand, @RequestParam String model2,
                         @RequestParam String variant, @RequestParam int year,
                         @RequestParam String color, @RequestParam String registrationNumber,
                         @RequestParam String pucNumber, @RequestParam String pucExpiry,
                         @RequestParam String rcExpiry, @RequestParam String insuranceNumber,
                         @RequestParam String insuranceExpiry, @RequestParam String fuelType,
                         @RequestParam String transmission, @RequestParam int seats,
                         @RequestParam String engineCC, @RequestParam double mileage,
                         @RequestParam BigDecimal pricePerDay, @RequestParam String location,
                         @RequestParam(required = false) String carImage,
                         @RequestParam(required = false) String description,
                         HttpSession session, RedirectAttributes ra) {
        User owner = getOwner(session);
        if (owner == null) return "redirect:/login";

        try {
            Car car = new Car();
            car.setOwner(owner);
            car.setBrand(brand);
            car.setModel(model2);
            car.setVariant(variant);
            car.setYear(year);
            car.setColor(color);
            car.setRegistrationNumber(registrationNumber);
            car.setPucNumber(pucNumber);
            car.setPucExpiry(pucExpiry);
            car.setRcExpiry(rcExpiry);
            car.setInsuranceNumber(insuranceNumber);
            car.setInsuranceExpiry(insuranceExpiry);
            car.setFuelType(Car.FuelType.valueOf(fuelType));
            car.setTransmission(Car.TransmissionType.valueOf(transmission));
            car.setSeats(seats);
            car.setEngineCC(engineCC);
            car.setMileage(mileage);
            car.setPricePerDay(pricePerDay);
            car.setLocation(location);
            car.setCarImage(carImage);
            car.setDescription(description);
            car.setAvailable(true);
            car.setApproved(true);
            carService.saveCar(car);
            ra.addFlashAttribute("success", "Car listed successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to add car: " + e.getMessage());
        }
        return "redirect:/owner/dashboard";
    }

    @PostMapping("/cars/{id}/toggle")
    public String toggleAvailability(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        User owner = getOwner(session);
        if (owner == null) return "redirect:/login";

        carService.findById(id).ifPresent(car -> {
            if (car.getOwner().getId().equals(owner.getId())) {
                car.setAvailable(!car.isAvailable());
                carService.saveCar(car);
            }
        });
        ra.addFlashAttribute("success", "Car availability updated.");
        return "redirect:/owner/dashboard";
    }
}
