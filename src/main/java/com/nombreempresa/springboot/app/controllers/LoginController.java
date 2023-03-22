package com.nombreempresa.springboot.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "error", required = false) String error, Model model, Principal principal,
			RedirectAttributes flash) {

		if (principal != null) {
			flash.addFlashAttribute("info", "Ya ha iniciado sesion anteriormente.");
			return "redirect:/";
		}

		if (error != null) {
			model.addAttribute("error", "Nombre de usuario o contraseña incorrectas.");
		}

		if (logout != null) {
			model.addAttribute("success", "Ha cerrado la sesion.");
		}
		return "login";
	}
}