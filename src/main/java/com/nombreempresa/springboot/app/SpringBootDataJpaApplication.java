package com.nombreempresa.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.nombreempresa.springboot.app.models.service.IUploadFileService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {

	@Autowired
	IUploadFileService uploadFileService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();

		// Generacion de contraseñas con Spring JDBC
//		String password = "123456";
//
//		for (int i = 0; i < 2; i++) {
//			String bcryptPassword = passwordEncoder.encode(password);
//			System.out.println(bcryptPassword);
//		}
	}

}
