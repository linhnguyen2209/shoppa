package edu.poly.controller.sites;

import java.text.DecimalFormat;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.poly.domain.Product;
import edu.poly.model.ProductDto;
import edu.poly.service.ProductService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/shopaa/sites/product-detail")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{productId}")
public String getProductDetails(@PathVariable("productId") Long productId, Model model) {
    Optional<Product> opt = productService.findById(productId);
    if (opt.isPresent()) {
        Product product = opt.get();
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);

        // Calculate the new price and round it using Math.round
        double newPrice = productDto.getPrice() * (1 - productDto.getDiscount());
        long roundedNewPrice = Math.round(newPrice);

        // Format the rounded price with a dollar sign
        DecimalFormat df = new DecimalFormat("$#,##0");
        String formattedNewPrice = df.format(roundedNewPrice);

        model.addAttribute("product", productDto);
        model.addAttribute("newPrice", formattedNewPrice);
        return "sites/products/product-detail";
    } else {
        model.addAttribute("error", "Đã xảy ra lỗi!");
        return "sites/home/home-page";
    }
}

}
