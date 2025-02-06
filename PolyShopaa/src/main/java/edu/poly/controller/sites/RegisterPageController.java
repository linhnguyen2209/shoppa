package edu.poly.controller.sites;

import edu.poly.model.RegisterDto;
import edu.poly.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shopaa/sites")
public class RegisterPageController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "sites/users/sign_up";
    }

    @PostMapping("/register")
    public String registerUserAccount(@Valid RegisterDto registerDto, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Thông tin đăng ký không hợp lệ!");
            return "sites/users/sign_up";
        }
        String saveResult = userService.saveRegister(registerDto); // lấy và in kết quả khi lưu vào csdl
        if (!saveResult.equals("")) {
            model.addAttribute("error", saveResult);
            return "sites/users/sign_up";
        }
        redirectAttributes.addFlashAttribute("message",
                "Đăng ký tài khoản thành công, vui lòng đăng nhâp để tiếp tục!");

        return "redirect:/shopaa/sites/login";
    }
}
