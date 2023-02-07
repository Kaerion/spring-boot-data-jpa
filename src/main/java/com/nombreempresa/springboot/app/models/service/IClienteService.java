package com.nombreempresa.springboot.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.nombreempresa.springboot.app.models.entity.Cliente;

public interface IClienteService {

	public List<Cliente> findAll();

	public Page<Cliente> findAll(PageRequest pageable);

	public void save(Cliente cliente);

	public Cliente findById(Long id);

	public void delete(Long id);
}
