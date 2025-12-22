package com.example.demo.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@Controller("UserController")
public class ProductController {
	
    @Autowired
    private ProductRepository productRepository;

	@GetMapping("/")
	public String topView(Model model, @PageableDefault(page = 0, size = 20) Pageable pageable) {
        Page<Product> productsPage = productRepository.findByStatus(0, pageable);
        model.addAttribute("page", productsPage);
        model.addAttribute("products", productsPage.getContent());
		return "user/index";
	}
}
