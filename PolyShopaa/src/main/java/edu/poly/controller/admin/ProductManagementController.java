package edu.poly.controller.admin;

import edu.poly.domain.Category;
import edu.poly.domain.Product;
import edu.poly.model.ProductDto;
import edu.poly.service.ProductService;
import edu.poly.service.CategoryService;

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
@RequestMapping("/shopaa/admin/products")
public class ProductManagementController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Đọc giá trị từ file cấu hình app.properties để xác định thư mục lưu trữ file
    // tải lên.
    @Value("${upload.dir}")
    private String uploadDir;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        if (page < 0) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (!keyword.isEmpty()) {
            productPage = productService.search(keyword, pageable);
        } else {
            productPage = productService.findAll(pageable);
        }

        ProductDto productDto = new ProductDto();
        productDto.setImage("/uploads/products/image_default.png");

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("productPage", productPage);
        model.addAttribute("productDto", productDto);
        model.addAttribute("isEditing", false);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", categoryService.findAll()); // list combobox

        return "admin/products/productManagement";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("keyword", keyword);
        return "redirect:/shopaa/admin/products";
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid @ModelAttribute("productDto") ProductDto productDto, BindingResult result,
            RedirectAttributes redirectAttributes, ModelMap model) {
        if (result.hasErrors()) {
            // Chuyển hướng lại trang productManagement với các lỗi xác thực
            productDto.setImage("/uploads/products/image_default.png");
            updateAttribute(productDto, model, false);
            model.addAttribute("error", "Đã xảy ra lỗi hãy kiểm tra lại các trường và hình ảnh!");
            return new ModelAndView("admin/products/productManagement", model);
        }
        String oldImg = "";
        boolean isEditing = false;
        Product product = new Product();
        if (productDto.getProductId() != 0) {
            Optional<Product> opt = productService.findById(productDto.getProductId());
            if (opt.isPresent()) {
                product = opt.get();
                isEditing = true;
                oldImg = product.getImage(); // lưu hình cũ
            }
        }
        boolean chooseFile = false;
        // // LƯU FILE HÌNH
        if (!productDto.getImageFile().isEmpty()) {
            chooseFile = true;
            MultipartFile file = productDto.getImageFile();
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
                productDto.setImage("/uploads/products/image_default.png");
                updateAttribute(productDto, model, isEditing);
                return new ModelAndView("admin/products/productManagement", model);
            } else {
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                // Tạo tên bằng id. Nếu là edit thì giữ nguyên tên còn create thì tăng lên 1.
                long productFilenameId = 0;
                if (productService.getMaxProductId() != null) {
                    productFilenameId = productService.getMaxProductId();
                }
                if (!isEditing) {
                    productFilenameId += 1;
                }
                // Tạo tên file mới với productId và số ngẫu nhiên
                String newFilename = "product_" + productFilenameId + fileExtension;
                // đối tượng Path đại diện cho đường dẫn đầy đủ của tệp tin.
                Path path = Paths.get(uploadDir, newFilename);

                try {
                    // Tạo thư mục đích nếu chưa tồn tại
                    if (!Files.exists(path.getParent())) {
                        Files.createDirectories(path.getParent());
                    }

                    // Sao chép tệp ảnh vào thư mục đích
                    file.transferTo(path.toFile());

                    // Cập nhật đường dẫn của tệp ảnh trong đối tượng productDto
                    productDto.setImage("/uploads/products/" + newFilename);
                    productDto.setImageFile(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        BeanUtils.copyProperties(productDto, product);
        if (!chooseFile) {
            product.setImage(oldImg); // nếu k chọn hình mới thì sẽ lấy lại hình cũ
        }
        Optional<Category> categoryOpt = categoryService.findById(productDto.getCategoryId());
        if (categoryOpt.isPresent()) {
            product.setCategory(categoryOpt.get());
        } else {
            model.addAttribute("error", "Danh mục của sản phẩm này không tồn tại!");
            productDto.setImage("/uploads/products/image_default.png");
            updateAttribute(productDto, model, isEditing);
            return new ModelAndView("admin/products/productManagement", model);
        }
        product.setUpdateAt(new Date());
        if(!isEditing){
            product.setCreateAt(new Date());
        }
        productService.save(product);
        redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được lưu!");
        System.out.println("Hello2");
        return new ModelAndView("redirect:/shopaa/admin/products");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editForm(@PathVariable Long id, RedirectAttributes redirectAttributes, ModelMap model) {
        Optional<Product> productOpt = productService.findById(id);

        if (productOpt.isPresent()) {
            Product entity = productOpt.get();
            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(entity, productDto);
            // set lại id
            productDto.setCategoryId(entity.getCategory().getCategoryId());
            updateAttribute(productDto, model, true);
            return new ModelAndView("admin/products/productManagement", model);
        }

        redirectAttributes.addFlashAttribute("message", "Sản phẩm không tồn tại!");
        return new ModelAndView("redirect:/shopaa/admin/products");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được xóa!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không thể xóa!");
            e.fillInStackTrace();
        }

        return "redirect:/shopaa/admin/products";
    }

    @GetMapping("/reset")
    public String reset() {
        return "redirect:/shopaa/admin/products";
    }

    public void updateAttribute(ProductDto productDto, ModelMap model, boolean isEditing) {
        // Chuyển hướng lại trang productManagement với các lỗi xác thực
        Page<Product> productPage = productService.findAll(PageRequest.of(0, 10));
        model.addAttribute("productPage", productPage);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("isEditing", isEditing);
        model.addAttribute("productDto", productDto);
        model.addAttribute("categories", categoryService.findAll()); // Add this line
    }
}
