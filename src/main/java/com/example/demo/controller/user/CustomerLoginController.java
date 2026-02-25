package com.example.demo.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.form.UserRegistForm;

import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerLoginController {

	@Autowired
	private HttpSession session;

	@GetMapping("/login")
	public String login(Model model, UserRegistForm form) {
		model.addAttribute(form);
		return "user/user/login";
	}

	@GetMapping(value = "/login", params = "error")
	public String loginError(Model model, UserRegistForm form) {
		var errorInfo = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		model.addAttribute(form);
		model.addAttribute("errorMsg", errorInfo.getMessage());
		return "user/user/login";
	}
}
