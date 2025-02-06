package edu.poly.controller.admin;

import edu.poly.domain.User;
import edu.poly.domain.UserProfile;
import edu.poly.model.UserDto;
import edu.poly.model.UserProfileDto;
import edu.poly.service.UserService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/shopaa/admin/users")
public class UserManagementController {
    @Autowired
    private UserService userService;

    @Value("${uploadAvt.dir}")
    private String uploadAvtDir;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        if (page < 0) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;

        if (!keyword.isEmpty()) {
            userPage = userService.search(keyword, pageable);
        } else {
            userPage = userService.findAll(pageable);
        }

        UserDto userDto = new UserDto();
        userDto.setRole("CUSTOMER");
        userDto.setStatus(1); // active

        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setAvatar("/uploads/users/avatar_default.png");
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("userPage", userPage);
        model.addAttribute("userDto", userDto);
        model.addAttribute("userProfileDto", userProfileDto);
        model.addAttribute("isEditing", false);
        model.addAttribute("keyword", keyword);

        return "admin/users/userManagement";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("keyword", keyword);
        return "redirect:/shopaa/admin/users";
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result,
            @ModelAttribute("userProfileDto") UserProfileDto userProfileDto,
            RedirectAttributes redirectAttributes, ModelMap model) {
        if (result.hasErrors()) {
            userProfileDto.setAvatar("/uploads/users/avatar_default.png");
            updateAttribute(userDto, userProfileDto, model, true);
            return new ModelAndView("admin/users/userManagement", model);
        }
        User user = new User();
        if (!userDto.getUsername().equals("")) {
            Optional<User> opt = userService.findById(userDto.getUsername());
            if (opt.isPresent()) {
                user = opt.get();
                BeanUtils.copyProperties(userDto, user);
                userService.save(user);
                redirectAttributes.addFlashAttribute("message", "Cập nhật người dùng thành công!");
                return new ModelAndView("redirect:/shopaa/admin/users");
            }
        }
        redirectAttributes.addFlashAttribute("error", "Người dùng này không tồn tại!");
        return new ModelAndView("redirect:/shopaa/admin/users");

    }

    @PostMapping("/saveProfile")
    public ModelAndView save(@Valid @ModelAttribute("userProfileDto") UserProfileDto userProfileDto,
            BindingResult result, @ModelAttribute("userDto") UserDto userDto,
            RedirectAttributes redirectAttributes, ModelMap model) {
        if (result.hasErrors()) {
            Optional<User> opt = userService.findById(userProfileDto.getUsername());
            UserDto userDto1 = new UserDto();
            BeanUtils.copyProperties(opt, userDto1);
            userProfileDto.setAvatar("/uploads/users/avatar_default.png");
            updateAttribute(userDto1, userProfileDto, model, true);
            return new ModelAndView("admin/users/userManagement", model);
        }
        boolean chooseFile = false;
        // LƯU FILE HÌNH
        if (!userProfileDto.getAvatarFile().isEmpty()) {
            chooseFile = true;
            MultipartFile file = userProfileDto.getAvatarFile();
            String originalFilename = file.getOriginalFilename();

            // Kiểm tra xem phần mở rộng của file có trong danh sách các phần mở rộng ảnh
            // không
            String[] allowedExtensions = { ".jpg", ".jpeg", ".png", ".gif", ".webp" }; // Danh sách phần mở rộng ảnh cho
                                                                                       // phép
            boolean isImage = false;
            for (String extension : allowedExtensions) {
                if (originalFilename.toLowerCase().endsWith(extension)) {
                    isImage = true;
                    break;
                }
            }

            if (!isImage) {
                model.addAttribute("error",
                        "File nhập vào bắt buộc phải là 1 hình ảnh!");
                userProfileDto.setAvatar("/uploads/users/avatar_default.png");
                updateAttribute(userDto, userProfileDto, model, isImage);
                return new ModelAndView("admin/users/userManagement", model);
            } else {
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                // Tạo tên bằng id. Nếu là edit thì giữ nguyên tên còn create thì tăng lên 1.
                String newFilename = "avatar_" + userProfileDto.getUsername() + fileExtension;
                // đối tượng Path đại diện cho đường dẫn đầy đủ của tệp tin.
                Path path = Paths.get(uploadAvtDir, newFilename);

                try {
                    // Tạo thư mục đích nếu chưa tồn tại
                    if (!Files.exists(path.getParent())) {
                        Files.createDirectories(path.getParent());
                    }

                    // Sao chép tệp ảnh vào thư mục đích
                    file.transferTo(path.toFile());

                    // Cập nhật đường dẫn của tệp ảnh trong đối tượng userProfileDto
                    userProfileDto.setAvatar("/uploads/users/" + newFilename);
                    userProfileDto.setAvatarFile(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (userProfileDto.getProfileId() != 0 && !userProfileDto.getUsername().equals("")) {
            UserProfile userProfile = userService.findProfileByUsername(userProfileDto.getUsername());
            if (userProfile != null) {
                String oldAvatar = userProfile.getAvatar();
                BeanUtils.copyProperties(userProfileDto, userProfile);
                // nếu k có hình mới thì lấy lại hình cũ
                if (!chooseFile) {
                    userProfile.setAvatar(oldAvatar);
                }
                userProfile.setUpdatedAt(new Date());
                userService.saveUserProfile(userProfile);
                redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin người dùng thành công!");
                return new ModelAndView("redirect:/shopaa/admin/users");
            }
        }
        redirectAttributes.addFlashAttribute("error", "Người dùng này không tồn tại!");
        return new ModelAndView("redirect:/shopaa/admin/users");

    }

    @GetMapping("/edit/{username}")
    public ModelAndView editForm(@PathVariable String username, RedirectAttributes redirectAttributes, ModelMap model) {
        Optional<User> UserOpt = userService.findById(username);

        if (UserOpt.isPresent()) {
            User entity = UserOpt.get();
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(entity, userDto);
            // lấy profile
            UserProfileDto userProfileDto = new UserProfileDto();
            UserProfile userProfile = userService.findProfileByUsername(username);
            BeanUtils.copyProperties(userProfile, userProfileDto);
            userProfileDto.setUsername(userProfile.getUser().getUsername());// phải làm thế này vì username không phải
                                                                            // thuộc tính trực tiếp của userprofile nên
                                                                            // k copy được
            updateAttribute(userDto, userProfileDto, model, true);
            return new ModelAndView("admin/users/userManagement", model);
        }

        redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại!");
        return new ModelAndView("redirect:/shopaa/admin/users");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Người dùng này đã được xóa!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Người dùng này không thể xóa!");
            e.fillInStackTrace();
        }

        return "redirect:/shopaa/admin/users";
    }

    @GetMapping("/reset")
    public String reset() {
        return "redirect:/shopaa/admin/users";
    }

    public void updateAttribute(UserDto userDto, UserProfileDto userProfileDto, ModelMap model, boolean isEditing) {
        Page<User> userPage = userService.findAll(PageRequest.of(0, 10));
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("userPage", userPage);
        model.addAttribute("userDto", userDto);
        model.addAttribute("userProfileDto", userProfileDto);
        model.addAttribute("isEditing", isEditing);
    }
}
