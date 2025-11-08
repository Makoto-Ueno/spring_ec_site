package com.example.demo.controller.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("UserController")
public class ProductController {

	@GetMapping("/")
	public String topView(Model model, @PageableDefault(page = 0, size = 20) Pageable pageable) {
		return "user/index";
	}
}
