package com.wheelzy.repository;

import com.wheelzy.model.Booking;
import com.wheelzy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRenterOrderByBookedAtDesc(User renter);
    List<Booking> findByCarOwnerOrderByBookedAtDesc(User owner);

    @Query("SELECT b FROM Booking b WHERE b.car.owner = :owner AND b.status = 'PENDING' ORDER BY b.bookedAt DESC")
    List<Booking> findPendingBookingsForOwner(User owner);

    @Query("SELECT b FROM Booking b WHERE b.renter = :renter ORDER BY b.bookedAt DESC")
    List<Booking> findByRenterOrderByDate(User renter);
}
