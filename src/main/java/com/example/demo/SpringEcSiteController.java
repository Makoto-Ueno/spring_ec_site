package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpringEcSiteController {

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/admin")
	public String indexAdmin() {
		return "admin";
	}
}