package com.wheelzy.controller;

import com.wheelzy.model.Car;
import com.wheelzy.model.User;
import com.wheelzy.service.CarService;
import com.wheelzy.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public String listCars(Model model, HttpSession session,
                           @RequestParam(required = false) String search,
                           @RequestParam(required = false) String fuel) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("unreadCount", notificationService.getUnreadCount(user));
        }

        if (search != null && !search.isEmpty()) {
            model.addAttribute("cars", carService.searchCars(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("cars", carService.getAllAvailableCars());
        }
        return "cars/list";
    }

    @GetMapping("/{id}")
    public String carDetail(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("unreadCount", notificationService.getUnreadCount(user));
        }

        return carService.findById(id).map(car -> {
            model.addAttribute("car", car);
            return "cars/detail";
        }).orElse("redirect:/cars");
    }
}
