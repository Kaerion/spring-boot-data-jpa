package com.nombreempresa.springboot.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Bean
	public UserDetailsService configurerGlobal(PasswordEncoder encoder) {
		User admin = (User) User.withUsername("admin").password(encoder.encode("admin")).roles("ADMIN", "USER").build();
		User user = (User) User.withUsername("user").password(encoder.encode("user")).roles("USER").build();
		return new InMemoryUserDetailsManager(admin, user);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
				.requestMatchers("/ver/**").hasAnyRole("USER").requestMatchers("/uploads/**").hasAnyRole("USER")
				.requestMatchers("/form/**").hasAnyRole("ADMIN").requestMatchers("/eliminar/**").hasAnyRole("ADMIN")
				.requestMatchers("/factura/**").hasAnyRole("ADMIN").anyRequest().authenticated().and().formLogin()
				.permitAll().and().logout().permitAll();
		return http.build();
	}
}
