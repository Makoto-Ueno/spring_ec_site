package com.example.demo.controller.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String indexAdmin(Model model, @PageableDefault(page = 0, size = 20) Pageable pageable) {
		Page<Product> productsPage = productRepository.findAll(pageable);
		model.addAttribute("page", productsPage);
		model.addAttribute("products", productsPage.getContent());
		return "admin/product/index";
	}

	@GetMapping("/admin/product/new")
	public String registView(Model model, ProductRegistForm form) {
		model.addAttribute(form);
		return "admin/product/regist";
	}

	@PostMapping("/admin/product/new")
	public String register(Model model, @ModelAttribute @Validated ProductRegistForm form, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

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

		redirectAttributes.addFlashAttribute("successed", true);

		return "redirect:/admin";
	}

	@GetMapping("/admin/product/{id}")
	public String changeView(Model model, @PathVariable int id) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isEmpty()) {
			// TODO:商品がなかった時の処理
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
		return "admin/product/change";
	}

	@PostMapping("/admin/product/{id}")
	public String change(Model model, @ModelAttribute @Validated ProductRegistForm form, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute(form);
			return "admin/product/{id}";
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

		redirectAttributes.addFlashAttribute("successed", true);

		return "redirect:/change";
	}

}