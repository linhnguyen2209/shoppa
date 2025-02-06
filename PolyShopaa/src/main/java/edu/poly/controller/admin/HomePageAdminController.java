package edu.poly.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shopaa/admin")
public class HomePageAdminController {
    @GetMapping("/home")
    public String home() {
        return "admin/home/home-page-admin";
    }
}
