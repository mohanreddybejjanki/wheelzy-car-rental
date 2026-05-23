package com.wheelzy.controller;

import com.wheelzy.model.Booking;
import com.wheelzy.model.Car;
import com.wheelzy.model.User;
import com.wheelzy.service.BookingService;
import com.wheelzy.service.CarService;
import com.wheelzy.service.NotificationService;
import com.wheelzy.service.PdfReceiptService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired private BookingService bookingService;
    @Autowired private CarService carService;
    @Autowired private NotificationService notificationService;
    @Autowired private PdfReceiptService pdfReceiptService;

    // Show booking form
    @GetMapping("/book/{carId}")
    public String bookingForm(@PathVariable Long carId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        if (user.getRole() == User.Role.OWNER) {
            return "redirect:/cars/" + carId + "?error=Owners+cannot+rent+cars";
        }

        return carService.findById(carId).map(car -> {
            if (!car.isAvailable())
                return "redirect:/cars/" + carId + "?error=Car+not+available";

            model.addAttribute("car", car);
            model.addAttribute("user", user);
            model.addAttribute("unreadCount", notificationService.getUnreadCount(user));
            model.addAttribute("today", LocalDate.now().toString());

            return "bookings/book";
        }).orElse("redirect:/cars");
    }

    // Submit booking
    @PostMapping("/book/{carId}")
    public String submitBooking(@PathVariable Long carId,
                                @RequestParam String startDate,
                                @RequestParam String endDate,
                                @RequestParam(required = false) String renterNote,
                                HttpSession session,
                                RedirectAttributes ra) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            Optional<Car> carOpt = carService.findById(carId);
            if (carOpt.isEmpty()) {
                ra.addFlashAttribute("error", "Car not found");
                return "redirect:/cars";
            }

            Car car = carOpt.get();
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            if (!end.isAfter(start)) {
                ra.addFlashAttribute("error", "End date must be after start date (minimum 1 day)");
                return "redirect:/bookings/book/" + carId;
            }

            Booking booking = new Booking();
            booking.setCar(car);
            booking.setRenter(user);
            booking.setStartDate(start);
            booking.setEndDate(end);
            booking.setPricePerDay(car.getPricePerDay());
            booking.setPickupLocation(car.getLocation() + ", Hyderabad");
            booking.setDropLocation(car.getLocation() + ", Hyderabad");
            booking.setRenterNote(renterNote);

            Booking saved = bookingService.createBooking(booking);

            ra.addFlashAttribute("success",
                    "Booking request sent! Ref: " + saved.getBookingReference());

            return "redirect:/bookings/my";

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Booking failed: " + e.getMessage());
            return "redirect:/bookings/book/" + carId;
        }
    }

    // Renter: my bookings
    @GetMapping("/my")
    public String myBookings(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        model.addAttribute("bookings", bookingService.getBookingsForRenter(user));
        model.addAttribute("unreadCount", notificationService.getUnreadCount(user));

        return "bookings/my-bookings";
    }

    // Download PDF receipt (FIXED)
    @GetMapping("/{id}/receipt")
    public ResponseEntity<byte[]> downloadReceipt(@PathVariable Long id, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).<byte[]>build();
        }

        return bookingService.findById(id).map(booking -> {

            if (!booking.getRenter().getId().equals(user.getId()) &&
                    !booking.getCar().getOwner().getId().equals(user.getId())) {

                return ResponseEntity.status(403).<byte[]>build();
            }

            try {
                byte[] pdf = pdfReceiptService.generateReceipt(booking);

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=receipt-" +
                                        booking.getBookingReference() + ".pdf")
                        .body(pdf);

            } catch (Exception e) {
                return ResponseEntity.status(500).<byte[]>build();
            }

        }).orElse(ResponseEntity.notFound().<byte[]>build());
    }

    // Cancel booking
    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes ra) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            bookingService.cancelBooking(id, user);
            ra.addFlashAttribute("success", "Booking cancelled successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/bookings/my";
    }
}