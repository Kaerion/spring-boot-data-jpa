package com.nombreempresa.springboot.app;

import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addResourceHandlers(registry);

		// Configuracion para que el directorio donde se guardaran las imagenes sea en
		// la maquina local
		// registry.addResourceHandler("/uploads/**").addResourceLocations("file:/D:/Desarrollo/uploads/");

		// Configuracion para carpeta en la raiz del proyecto. Este metodo seria mas
		// dinamico ya que no requiere la creacion por parte del usuario de la carpeta
		// contenedora de imagenes pero debe existir la carpeta creada dentro del
		// proyecto
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		log.info(resourcePath);
		registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
	}

}
