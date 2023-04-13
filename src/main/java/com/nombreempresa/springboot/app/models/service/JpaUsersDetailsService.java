package com.nombreempresa.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nombreempresa.springboot.app.models.dao.IUsuarioDao;
import com.nombreempresa.springboot.app.models.entity.Role;
import com.nombreempresa.springboot.app.models.entity.Usuario;

// la interfaz UserDetailsService carga el usuario mapeado con Spring JPA
@Service("jpaUserDetailsService")
public class JpaUsersDetailsService implements UserDetailsService {

	@Autowired
	IUsuarioDao usuarioDao;

	private Logger log = LoggerFactory.getLogger(JpaUsersDetailsService.class);

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioDao.findByUsername(username);

		if (usuario == null) {
			log.error("Error login: No existe el usuario: " + username);
			throw new UsernameNotFoundException("Username " + username + " no existe en la BD");
		}

		// La interfaz GrantedAuthority da informacion de roles en Spring Security.
		// Para utilizar la clase simple de esta interfaz, usamos SimpleGrantedAuthority
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (Role role : usuario.getRoles()) {
			log.info("Role: " + role.getAuthority());
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}

		if (authorities.isEmpty()) {
			log.error("Error login: El usuario: " + username + "no tiene roles asignados.");
			throw new UsernameNotFoundException("Username " + username + " no tiene roles asignados");
		}

		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true,
				authorities);
	}

}
