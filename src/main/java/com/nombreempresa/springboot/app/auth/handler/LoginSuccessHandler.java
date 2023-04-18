package com.nombreempresa.springboot.app.auth.handler;

import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		SessionFlashMapManager flashMapManager = new SessionFlashMapManager();

		FlashMap flashMap = new FlashMap();

		// Se define el locale de esta forma ya que no funciona pasarlo por parametro.
		Locale locale = localeResolver.resolveLocale(request);

		flashMap.put("success",
				messageSource.getMessage("login.flash.signok", null, locale) + authentication.getName());

		flashMapManager.saveOutputFlashMap(flashMap, request, response);

		if (authentication != null) {
			logger.info("El usuario " + authentication.getName() + " ha iniciado sesion con exito.");
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}

}
