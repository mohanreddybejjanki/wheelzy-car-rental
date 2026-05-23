package com.wheelzy.repository;

import com.wheelzy.model.Car;
import com.wheelzy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByOwner(User owner);
    List<Car> findByAvailableTrueAndApprovedTrue();
    List<Car> findByAvailableTrueAndApprovedTrueAndBrandContainingIgnoreCase(String brand);

    @Query("SELECT c FROM Car c WHERE c.available = true AND c.approved = true AND " +
           "(LOWER(c.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.model) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Car> searchCars(String keyword);

    @Query("SELECT c FROM Car c WHERE c.available = true AND c.approved = true AND " +
           "(:fuelType IS NULL OR c.fuelType = :fuelType)")
    List<Car> findByFuelType(Car.FuelType fuelType);
}
