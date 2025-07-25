package com.example.demo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.form.UserRegistForm;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private HttpSession session;

	@GetMapping("/admin/login")
	public String login(Model model, UserRegistForm form) {
		model.addAttribute(form);
		return "admin/user/login";
	}

	@GetMapping(value = "/admin/login", params = "error")
	public String loginError(Model model, UserRegistForm form) {
		var errorInfo = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		model.addAttribute(form);
		model.addAttribute("errorMsg", errorInfo.getMessage());
		return "admin/user/login";
	}
}
