package com.nombreempresa.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nombreempresa.springboot.app.models.dao.IClienteCrudDao;
import com.nombreempresa.springboot.app.models.dao.IClienteDao;
import com.nombreempresa.springboot.app.models.dao.IFacturaDao;
import com.nombreempresa.springboot.app.models.dao.IProductoDao;
import com.nombreempresa.springboot.app.models.entity.Cliente;
import com.nombreempresa.springboot.app.models.entity.Factura;
import com.nombreempresa.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDao clienteDao;

	@Autowired
	private IClienteCrudDao clienteCrudDao;

	@Autowired
	private IProductoDao productoDao;

	@Autowired
	private IFacturaDao facturaDao;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteCrudDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	@Transactional // Al ser solo escritura no hace falta el readOnly
	public void save(Cliente cliente) {
		clienteCrudDao.save(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return clienteCrudDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteCrudDao.deleteById(id);
	}

	@Override
	public List<Producto> findByName(String term) {
		return productoDao.findByName(term);
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);

	}

}
