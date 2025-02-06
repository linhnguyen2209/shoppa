package edu.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import edu.poly.domain.Order;
import edu.poly.domain.ShoppingCartItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendOrderConfirmationEmail(String to, String subject, Order order, List<ShoppingCartItem> cartItems,
            String shippingAddress, double totalAmount) {
        Context context = new Context();
        context.setVariable("cartItems", cartItems);
        context.setVariable("shippingAddress", shippingAddress);
        context.setVariable("totalAmount", totalAmount);

        // Tạo base URL cho các hình ảnh
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        context.setVariable("baseUrl", baseUrl);

        String body = templateEngine.process("emails/order-confirmation", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true indicates that this is an HTML email
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
