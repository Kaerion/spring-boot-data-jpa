package com.nombreempresa.springboot.app.models.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nombreempresa.springboot.app.models.entity.Cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager em; // Consultas jpa a la clase entity, no a la tabla

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return em.createQuery("from Cliente").getResultList();
	}

	@Override
	@Transactional // Al ser solo escritura no hace falta el readOnly
	public void save(Cliente cliente) {
		// TODO Auto-generated method stub
		em.persist(cliente);
	}

}
