package com.nombreempresa.springboot.app.models.service;

import java.util.List;

import com.nombreempresa.springboot.app.models.entity.Cliente;

public interface IClienteService {

	public List<Cliente> findAll();

	public void save(Cliente cliente);

	public Cliente findById(Long id);

	public void delete(Long id);
}
