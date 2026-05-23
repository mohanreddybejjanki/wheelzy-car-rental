package com.wheelzy.service;

import com.wheelzy.model.Car;
import com.wheelzy.model.User;
import com.wheelzy.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllAvailableCars() {
        return carRepository.findByAvailableTrueAndApprovedTrue();
    }

    public List<Car> searchCars(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllAvailableCars();
        }
        return carRepository.searchCars(keyword.trim());
    }

    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    public List<Car> getCarsByOwner(User owner) {
        return carRepository.findByOwner(owner);
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
}
