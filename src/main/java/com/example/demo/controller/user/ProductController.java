package com.example.demo.controller.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.Product;
import com.example.demo.entity.Stock;
import com.example.demo.form.ProductRegistForm;
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
	@GetMapping("/{id}")
	public String changeView(Model model, @PathVariable int id) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isEmpty()) {
	        // ここで400エラー画面へ（400.html）遷移させる
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "商品が存在しません");
		}
		Product product = productOpt.get();
		ProductRegistForm form = new ProductRegistForm();
		form.setProductId(id);
		form.setName(product.getName());
		form.setAmount(product.getAmount());
		form.setDescription(product.getDescription());
		form.setImageUrl(product.getImageUrl());
		form.setStatus(product.getStatus());

		Stock stock = product.getStock();
		form.setQuantity(stock.getQuantity());

		model.addAttribute(form);
		return "user/product/detail";
	}
}
