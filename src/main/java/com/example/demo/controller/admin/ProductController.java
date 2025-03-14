package com.example.demo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.form.ProductRegistForm;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.StockRepository;

@Controller
public class ProductController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private StockRepository stockRepository;

	@GetMapping("/admin")
	public String indexAdmin() {
		return "admin/product/index";
	}

	@GetMapping("/admin/product/new")
	public String registView(Model model, ProductRegistForm form) {
		model.addAttribute(form);
		return "admin/product/regist";
	}

	@PostMapping("/admin/product/new")
	public String register(Model model, @ModelAttribute @Validated ProductRegistForm form,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			model.addAttribute(form);
			return "admin/product/regist";
		}

		Product product = new Product();
		product.setName(form.getName());
		product.setAmount(form.getAmount());
		product.setDescription(form.getDescription());
		product.setImageUrl(form.getImageUrl());
		product.setStatus(form.getStatus());
		productRepository.saveAndFlush(product);

		Stock stock = new Stock();
		stock.setQuantity(form.getQuantity());

		stock.setProductId(form.getProductId());
		stock.setProduct(product);
		stockRepository.saveAndFlush(stock);

		return "redirect:/admin";
	}

}