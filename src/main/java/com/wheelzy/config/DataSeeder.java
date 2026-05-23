package com.wheelzy.config;

import com.wheelzy.model.Car;
import com.wheelzy.model.User;
import com.wheelzy.repository.CarRepository;
import com.wheelzy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        // Seed 10 car owners + 2 renters
        User[] owners = new User[10];

        owners[0] = createUser("Rahul Sharma", "rahul@example.com", "rahul123", "9876543210", User.Role.OWNER, "Banjara Hills");
        owners[1] = createUser("Priya Reddy", "priya@example.com", "priya123", "9876543211", User.Role.OWNER, "Jubilee Hills");
        owners[2] = createUser("Kiran Kumar", "kiran@example.com", "kiran123", "9876543212", User.Role.OWNER, "Gachibowli");
        owners[3] = createUser("Sneha Patel", "sneha@example.com", "sneha123", "9876543213", User.Role.OWNER, "Madhapur");
        owners[4] = createUser("Arun Nair", "arun@example.com", "arun123", "9876543214", User.Role.OWNER, "Hitech City");
        owners[5] = createUser("Divya Menon", "divya@example.com", "divya123", "9876543215", User.Role.OWNER, "Kondapur");
        owners[6] = createUser("Vikram Singh", "vikram@example.com", "vikram123", "9876543216", User.Role.OWNER, "Kukatpally");
        owners[7] = createUser("Anita Joshi", "anita@example.com", "anita123", "9876543217", User.Role.OWNER, "Secunderabad");
        owners[8] = createUser("Ravi Teja", "ravi@example.com", "ravi123", "9876543218", User.Role.OWNER, "Ameerpet");
        owners[9] = createUser("Meena Iyer", "meena@example.com", "meena123", "9876543219", User.Role.OWNER, "Begumpet");

        for (User owner : owners) {
            userRepository.save(owner);
        }

        // Renters
        User renter1 = createUser("Test Renter", "renter@example.com", "renter123", "9000000001", User.Role.RENTER, "Ameerpet");
        renter1.setLicenseNumber("TS09 2020 123456");
        userRepository.save(renter1);

        User renter2 = createUser("Demo User", "demo@example.com", "demo123", "9000000002", User.Role.RENTER, "Dilsukhnagar");
        renter2.setLicenseNumber("TS10 2019 654321");
        userRepository.save(renter2);

        // Admin
        User admin = createUser("Admin Wheelzy", "admin@wheelzy.com", "admin123", "9000000000", User.Role.ADMIN, "Hyderabad");
        userRepository.save(admin);

        // Seed 10 cars
        seedCar(owners[0], "Maruti Suzuki", "Swift", "VXI", 2022, "Pearl White", "TS09AB1234",
                "PUC2024001", "31-Dec-2025", "1197cc", 22.5, Car.FuelType.PETROL, Car.TransmissionType.MANUAL,
                5, 1499.0, "Banjara Hills", "17.4156", "78.4347",
                "Well maintained Swift in excellent condition. Perfect for city drives around Hyderabad.",
                "https://images.unsplash.com/photo-1541348263662-e068662d82af?w=600");

        seedCar(owners[1], "Hyundai", "Creta", "SX", 2023, "Titan Grey", "TS09CD5678",
                "PUC2024002", "30-Jun-2026", "1497cc", 17.4, Car.FuelType.PETROL, Car.TransmissionType.AUTOMATIC,
                5, 2499.0, "Jubilee Hills", "17.4319", "78.4073",
                "Premium Creta SUV with sunroof and all modern features. Great for long drives.",
                "https://images.unsplash.com/photo-1609521263047-f8f205293f24?w=600");

        seedCar(owners[2], "Toyota", "Innova Crysta", "ZX", 2021, "Silver", "TS09EF9012",
                "PUC2024003", "31-Mar-2026", "2393cc", 14.8, Car.FuelType.DIESEL, Car.TransmissionType.MANUAL,
                7, 3499.0, "Gachibowli", "17.4401", "78.3489",
                "Spacious 7-seater Innova. Ideal for family trips and airport transfers.",
                "https://images.unsplash.com/photo-1625047509248-ec889cbff17f?w=600");

        seedCar(owners[3], "Tata", "Nexon EV", "Max XZ+", 2023, "Intensi Teal", "TS09GH3456",
                "PUC2024004", "31-Dec-2026", "Electric", 0.0, Car.FuelType.ELECTRIC, Car.TransmissionType.AUTOMATIC,
                5, 2799.0, "Madhapur", "17.4486", "78.3908",
                "Eco-friendly Nexon EV with 437km range. No fuel costs! Smooth and silent drive.",
                "https://images.unsplash.com/photo-1593941707882-a5bba14938c7?w=600");

        seedCar(owners[4], "Honda", "City", "ZX CVT", 2022, "Lunar Silver", "TS09IJ7890",
                "PUC2024005", "28-Feb-2026", "1498cc", 17.8, Car.FuelType.PETROL, Car.TransmissionType.AUTOMATIC,
                5, 1999.0, "Hitech City", "17.4478", "78.3762",
                "Elegant Honda City sedan with CVT automatic. Comfortable for business and leisure.",
                "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=600");

        seedCar(owners[5], "Kia", "Seltos", "HTX Plus", 2023, "Glacier White Pearl", "TS09KL2345",
                "PUC2024006", "30-Sep-2026", "1497cc", 16.8, Car.FuelType.PETROL, Car.TransmissionType.AUTOMATIC,
                5, 2299.0, "Kondapur", "17.4596", "78.3536",
                "Feature-packed Kia Seltos with panoramic sunroof, BOSE sound system and ADAS.",
                "https://images.unsplash.com/photo-1605559424843-9073c6e4d0f9?w=600");

        seedCar(owners[6], "Mahindra", "XUV700", "AX7 L", 2022, "Dazzling Silver", "TS09MN6789",
                "PUC2024007", "31-Jul-2026", "1997cc", 15.2, Car.FuelType.DIESEL, Car.TransmissionType.AUTOMATIC,
                7, 3999.0, "Kukatpally", "17.4849", "78.3998",
                "Powerful XUV700 with ADAS Level 2. Best SUV for highway tours from Hyderabad.",
                "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=600");

        seedCar(owners[7], "Maruti Suzuki", "Baleno", "Alpha", 2023, "Grandeur Grey", "TS09OP1234",
                "PUC2024008", "31-Oct-2025", "1197cc", 22.35, Car.FuelType.PETROL, Car.TransmissionType.AUTOMATIC,
                5, 1699.0, "Secunderabad", "17.4399", "78.4983",
                "Smart Baleno hatchback with HUD display. Perfect for daily city rides.",
                "https://images.unsplash.com/photo-1580273916550-e323be2ae537?w=600");

        seedCar(owners[8], "MG", "Hector", "Sharp Pro", 2023, "Starry Black", "TS09QR5678",
                "PUC2024009", "31-Aug-2026", "1451cc", 15.8, Car.FuelType.PETROL, Car.TransmissionType.MANUAL,
                5, 2799.0, "Ameerpet", "17.4374", "78.4482",
                "Feature-rich MG Hector with 14-inch touchscreen and panoramic sunroof. Great road presence.",
                "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?w=600");

        seedCar(owners[9], "Skoda", "Slavia", "Style 1.5 TSI AT", 2022, "Candy White", "TS09ST9012",
                "PUC2024010", "28-Nov-2025", "1498cc", 18.1, Car.FuelType.PETROL, Car.TransmissionType.AUTOMATIC,
                5, 2199.0, "Begumpet", "17.4358", "78.4651",
                "European premium sedan with turbo engine. Sporty yet comfortable for long drives.",
                "https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=600");

        System.out.println("✅ Wheelzy Rentals: Data seeded successfully!");
        System.out.println("📧 Owner login: rahul@example.com / rahul123");
        System.out.println("📧 Renter login: renter@example.com / renter123");
        System.out.println("📧 Admin login: admin@wheelzy.com / admin123");
    }

    private User createUser(String name, String email, String password, String phone, User.Role role, String address) {
        User user = new User(name, email, password, phone, role);
        user.setAddress(address);
        user.setCity("Hyderabad");
        return user;
    }

    private void seedCar(User owner, String brand, String model, String variant, int year, String color,
                          String rcNumber, String pucNumber, String pucExpiry, String engineCC,
                          double mileage, Car.FuelType fuel, Car.TransmissionType transmission,
                          int seats, double pricePerDay, String location, String lat, String lng,
                          String description, String imageUrl) {
        Car car = new Car();
        car.setOwner(owner);
        car.setBrand(brand);
        car.setModel(model);
        car.setVariant(variant);
        car.setYear(year);
        car.setColor(color);
        car.setRegistrationNumber(rcNumber);
        car.setPucNumber(pucNumber);
        car.setPucExpiry(pucExpiry);
        car.setRcExpiry("31-Dec-2034");
        car.setInsuranceNumber("INS-" + rcNumber);
        car.setInsuranceExpiry("31-Dec-2025");
        car.setEngineCC(engineCC);
        car.setMileage(mileage);
        car.setFuelType(fuel);
        car.setTransmission(transmission);
        car.setSeats(seats);
        car.setPricePerDay(BigDecimal.valueOf(pricePerDay));
        car.setLocation(location);
        car.setLatitude(lat);
        car.setLongitude(lng);
        car.setDescription(description);
        car.setCarImage(imageUrl);
        car.setAvailable(true);
        car.setApproved(true);
        carRepository.save(car);
    }
}
