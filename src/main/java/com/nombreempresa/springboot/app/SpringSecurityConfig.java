package com.nombreempresa.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nombreempresa.springboot.app.auth.handler.LoginSuccessHandler;
import com.nombreempresa.springboot.app.models.service.JpaUsersDetailsService;

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

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JpaUsersDetailsService usersDetailsService;

// 	Necesario para el metodo de JDBC
//	@Autowired
//	private DataSource dataSource;

	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {

		// (PasswordEncoder encode)
//		User admin = (User) User.withUsername("admin").password(encoder.encode("admin")).roles("ADMIN", "USER").build();
//		User user = (User) User.withUsername("user").password(encoder.encode("user")).roles("USER").build();
//		return new InMemoryUserDetailsManager(admin, user);

		// (AuthenticationManagerBuilder build) Para obtencion desde base de datos con
		// Spring JDBC
//		build.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
//				.usersByUsernameQuery("select username, password, enabled from users where username=?")
//				.authoritiesByUsernameQuery(
//						"select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username = ?");

		// Spring JPA
		build.userDetailsService(usersDetailsService).passwordEncoder(passwordEncoder);

	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar", "/locale")
				.permitAll()
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
