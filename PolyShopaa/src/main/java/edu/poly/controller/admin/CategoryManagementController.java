package edu.poly.controller.admin;

import edu.poly.domain.Category;
import edu.poly.model.CategoryDto;
import edu.poly.service.CategoryService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/shopaa/admin/categories")
public class CategoryManagementController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping
	public String list(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "") String keyword,
			Model model) {

		if (page < 0) {
			page = 0;
		}

		Pageable pageable = PageRequest.of(page, size);
		Page<Category> categoryPage;

		if (!keyword.isEmpty()) {
			categoryPage = categoryService.search(keyword, pageable);
		} else {
			categoryPage = categoryService.findAll(pageable);
		}

		model.addAttribute("categories", categoryPage.getContent());
		model.addAttribute("categoryPage", categoryPage);
		model.addAttribute("categoryDto", new CategoryDto());
		model.addAttribute("isEditing", false);
		model.addAttribute("keyword", keyword);

		return "admin/categories/categoryManagement";
	}

	@GetMapping("/search")
	public String search(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("keyword", keyword);
		return "redirect:/shopaa/admin/categories";
	}

	@PostMapping("/save")
	public ModelAndView save(@Valid @ModelAttribute("categoryDto") CategoryDto categoryDto, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			ModelMap model = new ModelMap();
			Page<Category> categoryPage = categoryService.findAll(PageRequest.of(0, 10));
			model.addAttribute("categoryPage", categoryPage);
			model.addAttribute("categories", categoryPage.getContent());
			model.addAttribute("isEditing", false);
			return new ModelAndView("admin/categories/categoryManagement", model);
		}

		Category category = new Category();
		if (categoryDto.getCategoryId() != 0) {
			Optional<Category> opt = categoryService.findById(categoryDto.getCategoryId());
			if (opt.isPresent()) {
				category = opt.get();
			}
		}
		BeanUtils.copyProperties(categoryDto, category);
		categoryService.save(category);
		redirectAttributes.addFlashAttribute("message", "Danh mục đã được lưu!");
		return new ModelAndView("redirect:/shopaa/admin/categories");
	}

	@GetMapping("/edit/{id}")
	public ModelAndView editForm(@PathVariable Long id, RedirectAttributes redirectAttributes, ModelMap model) {
		Optional<Category> categoryOpt = categoryService.findById(id);

		if (categoryOpt.isPresent()) {
			Category entity = categoryOpt.get();
			CategoryDto categoryDto = new CategoryDto();
			BeanUtils.copyProperties(entity, categoryDto);
			model.addAttribute("categoryDto", categoryDto);
			model.addAttribute("isEditing", true);

			Page<Category> categoryPage = categoryService.findAll(PageRequest.of(0, 10));
			model.addAttribute("categoryPage", categoryPage);
			model.addAttribute("categories", categoryPage.getContent());

			return new ModelAndView("admin/categories/categoryManagement", model);
		}

		redirectAttributes.addFlashAttribute("message", "Danh mục không tồn tại!");
		return new ModelAndView("redirect:/shopaa/admin/categories");
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			categoryService.deleteById(id);
			redirectAttributes.addFlashAttribute("message", "Danh mục này đã được xóa!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Danh mục này không thể xóa!");
			e.fillInStackTrace();
		}

		return "redirect:/shopaa/admin/categories";
	}

	@GetMapping("/reset")
	public String reset() {
		return "redirect:/shopaa/admin/categories";
	}
}
