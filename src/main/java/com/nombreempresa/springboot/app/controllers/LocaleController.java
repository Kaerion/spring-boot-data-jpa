package com.nombreempresa.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LocaleController {

	@GetMapping("/locale")
	public String locale(HttpServletRequest request) {
		String ultimaUrl = request.getHeader("referer"); // El parametro referer da la ultima url en el header
		return "redirect:" + ultimaUrl;
	}
}
