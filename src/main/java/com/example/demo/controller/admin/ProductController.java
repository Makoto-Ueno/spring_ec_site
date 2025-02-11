package com.example.demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

	@GetMapping("/admin")
	public String indexAdmin() {
		return "admin/product/index";
	}

	@GetMapping("/admin/product/new")
	public String ProductRegistAdmin() {
		return "admin/product/regist";
	}

}