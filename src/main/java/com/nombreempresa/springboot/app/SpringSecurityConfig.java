package com.nombreempresa.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nombreempresa.springboot.app.auth.handler.LoginSuccessHandler;

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // Habilita la seguridad mediante anotaciones en el
																	// controlador en lugar de
																	// en el metodo configure de esta clase. Si esta
																	// habilitado el prePostEnabled, en lugar de
																	// utilizar la anotacion Secure, se puede hacer lo
																	// mismo con la anotacion
																	// PreAuthorize("hasRole('ROLE_ADMIN')")
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

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
//				.requestMatchers("/ver/**").hasAnyRole("USER")
//				.requestMatchers("/uploads/**").hasAnyRole("USER")
//				.requestMatchers("/form/**").hasAnyRole("ADMIN")
//				.requestMatchers("/eliminar/**").hasAnyRole("ADMIN")
//				.requestMatchers("/factura/**").hasAnyRole("ADMIN")
				.anyRequest().authenticated().and().formLogin().successHandler(loginSuccessHandler).loginPage("/login")
				.permitAll().and().logout().permitAll().and().exceptionHandling().accessDeniedPage("/error_403");
		return http.build();
	}
}
