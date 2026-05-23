package com.wheelzy.controller;

import com.wheelzy.model.User;
import com.wheelzy.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public String notifications(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        notificationService.markAllAsRead(user);
        model.addAttribute("user", user);
        model.addAttribute("notifications", notificationService.getNotificationsForUser(user));
        model.addAttribute("unreadCount", 0L);
        return "notifications";
    }

    @PostMapping("/{id}/read")
    @ResponseBody
    public String markRead(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "error";
        notificationService.markAsRead(id);
        return "ok";
    }
}
