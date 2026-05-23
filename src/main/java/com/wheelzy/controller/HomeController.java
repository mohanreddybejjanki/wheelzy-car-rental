package com.wheelzy.controller;

import com.wheelzy.model.User;
import com.wheelzy.service.CarService;
import com.wheelzy.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private CarService carService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public String home(Model model, HttpSession session,
                       @RequestParam(required = false) String search) {
        User user = (User) session.getAttribute("user");

        if (search != null && !search.isEmpty()) {
            model.addAttribute("cars", carService.searchCars(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("cars", carService.getAllAvailableCars());
        }

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("unreadCount", notificationService.getUnreadCount(user));
        }

        return "index";
    }

    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("unreadCount", notificationService.getUnreadCount(user));
        }
        return "about";
    }
}
