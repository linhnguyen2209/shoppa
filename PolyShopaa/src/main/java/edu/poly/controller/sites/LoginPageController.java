package edu.poly.controller.sites;

import edu.poly.model.LoginDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/shopaa/sites")
public class LoginPageController {

    @Autowired
    HttpSession session;

    @GetMapping("/login")
    public String login(Model model) {
        String error = (String) session.getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error);
            session.removeAttribute("error");
        }

        model.addAttribute("loginDto", new LoginDto());
        return "sites/users/login";
    }

    @PostMapping("/login")
    public String authenticateUser(@ModelAttribute("loginDto") LoginDto loginDto,
            BindingResult result,
            HttpServletResponse response,
            HttpSession session,
            Model model) {
        if (result.hasErrors()) {
            return "sites/users/login";
        }

        // Spring Security sẽ quản lý xác thực, không cần kiểm tra ở đây
        // Người dùng sẽ được xác thực bằng Spring Security và chuyển hướng dựa trên vai
        // trò

        return "redirect:/shopaa/sites/home";
    }
}
