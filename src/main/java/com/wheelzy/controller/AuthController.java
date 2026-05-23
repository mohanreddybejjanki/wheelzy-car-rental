package com.wheelzy.controller;

import com.wheelzy.model.User;
import com.wheelzy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model,
                            @RequestParam(required = false) String error,
                            @RequestParam(required = false) String registered) {
        if (session.getAttribute("user") != null) return "redirect:/";
        if (error != null) model.addAttribute("error", "Invalid email or password.");
        if (registered != null) model.addAttribute("success", "Registration successful! Please login.");
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes ra) {
        User user = userService.login(email, password);
        if (user == null) {
            return "redirect:/login?error=true";
        }
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(3600);

        if (user.getRole() == User.Role.OWNER) return "redirect:/owner/dashboard";
        if (user.getRole() == User.Role.ADMIN) return "redirect:/admin/dashboard";
        return "redirect:/cars";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("user") != null) return "redirect:/";
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String phone,
                           @RequestParam String role,
                           @RequestParam(required = false) String licenseNumber,
                           @RequestParam(required = false) String address,
                           RedirectAttributes ra) {
        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhone(phone);
            user.setRole(User.Role.valueOf(role.toUpperCase()));
            user.setLicenseNumber(licenseNumber);
            user.setAddress(address);
            user.setCity("Hyderabad");
            userService.register(user);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
