package com.nombreempresa.springboot.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nombreempresa.springboot.app.models.service.IClienteService;
import com.nombreempresa.springboot.app.view.xml.ClienteList;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	// REST Controller
	@GetMapping(value = "/listar")
	// @ResponseBody no es necesario porque todos los metodos de una clase
	// @RestController lo heredan
	public ClienteList listarRest() {

		return new ClienteList(clienteService.findAll());
	}
}
