package com.nombreempresa.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nombreempresa.springboot.app.auth.filter.JWTAuthenticationFilter;
import com.nombreempresa.springboot.app.auth.handler.LoginSuccessHandler;
import com.nombreempresa.springboot.app.models.service.JpaUsersDetailsService;

@SuppressWarnings("deprecation")
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // Habilita la seguridad mediante anotaciones
																			// en el
// controlador en lugar de
// en el metodo configure de esta clase. Si esta
// habilitado el prePostEnabled, en lugar de
// utilizar la anotacion Secure, se puede hacer lo
// mismo con la anotacion
// PreAuthorize("hasRole('ROLE_ADMIN')")
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends GlobalMethodSecurityConfiguration {

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JpaUsersDetailsService usersDetailsService;

// 	Necesario para el metodo de JDBC
//	@Autowired
//	private DataSource dataSource;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usersDetailsService).passwordEncoder(passwordEncoder);
	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().requestMatchers("/", "/css/**", "/js/**", "/images/**", "/listar**", "/locale")
				.permitAll()
//				.requestMatchers("/ver/**").hasAnyRole("USER")
//				.requestMatchers("/uploads/**").hasAnyRole("USER")
//				.requestMatchers("/form/**").hasAnyRole("ADMIN")
//				.requestMatchers("/eliminar/**").hasAnyRole("ADMIN")
//				.requestMatchers("/factura/**").hasAnyRole("ADMIN")
				.anyRequest().authenticated()/*
												 * and().formLogin().successHandler(loginSuccessHandler).loginPage(
												 * "/login") //Se comenta el login ya que JWT da seguridad automatica.
												 * .permitAll().and().logout().permitAll().and().exceptionHandling().
												 * accessDeniedPage("/error_403")
												 */.and()
				.addFilter(new JWTAuthenticationFilter(authenticationManagerBean())).csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return authenticationManager();
	}
}
