package edu.poly.controller.sites;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.poly.domain.Product;
import edu.poly.service.ProductService;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/shopaa/sites")
public class HomePageController {
    @Autowired
    ProductService productService;

    @GetMapping("/home")
    public String home(ModelMap model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "sites/home/home-page";
    }
}
