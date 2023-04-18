package com.nombreempresa.springboot.app.controllers;

import java.security.Principal;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@Autowired
	MessageSource messageSource;

	@GetMapping("/login")
	public String login(@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "error", required = false) String error, Model model, Principal principal,
			RedirectAttributes flash, Locale locale) {

		if (principal != null) {
			flash.addFlashAttribute("info", messageSource.getMessage("login.flash.alreadysign", null, locale));
			return "redirect:/";
		}

		if (error != null) {
			model.addAttribute("error", messageSource.getMessage("login.flash.signerror", null, locale));
		}

		if (logout != null) {
			model.addAttribute("success", messageSource.getMessage("login.flash.signclosed", null, locale));
		}
		return "login";
	}
}
