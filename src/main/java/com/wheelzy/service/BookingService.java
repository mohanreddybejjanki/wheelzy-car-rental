package com.wheelzy.service;

import com.wheelzy.model.Booking;
import com.wheelzy.model.Car;
import com.wheelzy.model.Notification;
import com.wheelzy.model.User;
import com.wheelzy.repository.BookingRepository;
import com.wheelzy.repository.CarRepository;
import com.wheelzy.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public Booking createBooking(Booking booking) {
        booking.calculateTotals();
        Booking saved = bookingRepository.save(booking);

        // Notify owner
        User owner = booking.getCar().getOwner();
        String msg = booking.getRenter().getName() + " has requested to rent your " +
                booking.getCar().getDisplayName() + " from " +
                booking.getStartDate() + " to " + booking.getEndDate() +
                ". Total: ₹" + booking.getTotalAmount();
        Notification ownerNotif = new Notification(owner, "New Booking Request", msg,
                Notification.NotificationType.BOOKING_REQUEST, saved.getId());
        notificationRepository.save(ownerNotif);

        return saved;
    }

    @Transactional
    public Booking approveBooking(Long bookingId, User owner) {
        Optional<Booking> opt = bookingRepository.findById(bookingId);
        if (opt.isEmpty()) throw new RuntimeException("Booking not found");

        Booking booking = opt.get();
        if (!booking.getCar().getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        booking.setStatus(Booking.BookingStatus.APPROVED);
        booking.setApprovedAt(LocalDateTime.now());

        // Mark car as unavailable
        Car car = booking.getCar();
        car.setAvailable(false);
        carRepository.save(car);

        Booking saved = bookingRepository.save(booking);

        // Notify renter
        String msg = "Great news! Your booking for " + booking.getCar().getDisplayName() +
                " from " + booking.getStartDate() + " to " + booking.getEndDate() +
                " has been APPROVED by the owner. Booking Ref: " + booking.getBookingReference();
        Notification renterNotif = new Notification(booking.getRenter(), "Booking Approved! 🎉", msg,
                Notification.NotificationType.BOOKING_APPROVED, saved.getId());
        notificationRepository.save(renterNotif);

        return saved;
    }

    @Transactional
    public Booking rejectBooking(Long bookingId, User owner, String reason) {
        Optional<Booking> opt = bookingRepository.findById(bookingId);
        if (opt.isEmpty()) throw new RuntimeException("Booking not found");

        Booking booking = opt.get();
        booking.setStatus(Booking.BookingStatus.REJECTED);
        booking.setOwnerNote(reason);
        Booking saved = bookingRepository.save(booking);

        // Notify renter
        String msg = "Unfortunately, your booking request for " + booking.getCar().getDisplayName() +
                " has been declined by the owner. Reason: " + (reason != null ? reason : "Not specified");
        Notification renterNotif = new Notification(booking.getRenter(), "Booking Declined", msg,
                Notification.NotificationType.BOOKING_REJECTED, saved.getId());
        notificationRepository.save(renterNotif);

        return saved;
    }

    @Transactional
    public Booking cancelBooking(Long bookingId, User renter) {
        Optional<Booking> opt = bookingRepository.findById(bookingId);
        if (opt.isEmpty()) throw new RuntimeException("Booking not found");

        Booking booking = opt.get();
        booking.setStatus(Booking.BookingStatus.CANCELLED);

        // Make car available again if it was approved
        if (booking.getStatus() == Booking.BookingStatus.APPROVED) {
            Car car = booking.getCar();
            car.setAvailable(true);
            carRepository.save(car);
        }

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsForRenter(User renter) {
        return bookingRepository.findByRenterOrderByBookedAtDesc(renter);
    }

    public List<Booking> getBookingsForOwner(User owner) {
        return bookingRepository.findByCarOwnerOrderByBookedAtDesc(owner);
    }

    public List<Booking> getPendingBookingsForOwner(User owner) {
        return bookingRepository.findPendingBookingsForOwner(owner);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }
}
