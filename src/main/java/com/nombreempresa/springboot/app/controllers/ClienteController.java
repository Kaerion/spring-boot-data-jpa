package com.nombreempresa.springboot.app.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nombreempresa.springboot.app.models.entity.Cliente;
import com.nombreempresa.springboot.app.models.service.IClienteService;
import com.nombreempresa.springboot.app.util.paginator.PageRender;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("cliente") // Guardo la entidad de clientes en la sesion hasta que acaba de guardarlo o
								// editarlo
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	private final static String UPLOADS_FOLDER = "uploads";

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = clienteService.findById(id);
		if (cliente == null) {
			flash.addAttribute("error", "El cliente no existe");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Detalle de cliente: " + cliente.getNombre());

		return "ver";
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageable = PageRequest.of(page, 5);

		Page<Cliente> clientes = clienteService.findAll(pageable);

		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		String flashMessage = (cliente.getId() != null) ? "Cliente editado con exito!" : "Cliente creado con exito!";

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}

		if (!foto.isEmpty()) {
			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {
				Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
				File archivo = rootPath.toFile();

				if (archivo.exists() && archivo.canRead()) {
					archivo.delete();
				}
			}

//			Path directorioRecursos = Paths.get("src//main//resources//static/uploads");    Ruta interna del proyecto
//			String rootPath = directorioRecursos.toFile().getAbsolutePath();

//			String rootPath = "D://Desarrollo//uploads"; // Ruta directorio externo al proyecto. Ruta en maquina que
			// ejecuta el servicio

			String uniqueFileName = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename(); // Genero el nombre
																										// de la foto
																										// que se va a
																										// guardar de
																										// forma
																										// dinamica
			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(uniqueFileName); // Crear directorio dentro del root del
																				// proyecto
			Path rootAbsolutPath = rootPath.toAbsolutePath();
			log.info("Root path = " + rootPath.toString());
			log.info("Root absolute path = " + rootAbsolutPath.toString());
			log.info("Unique file name: " + uniqueFileName);

			try {
//				byte[] bytes = foto.getBytes();													//Logica para ña transformacion del archivo desde ruta interna o pc local
//				Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
//				Files.write(rutaCompleta, bytes);

				Files.copy(foto.getInputStream(), rootAbsolutPath);
				flash.addFlashAttribute("info", "Se ha subido correctamente la foto: " + uniqueFileName);

				cliente.setFoto(uniqueFileName);
			} catch (IOException e) {
				flash.addFlashAttribute("error", "Ha habido un fallo al subir la imagen: " + uniqueFileName);
				e.printStackTrace();
			}
		}

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", flashMessage);
		return "redirect:listar";
	}

	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findById(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El id del cliente no existe.");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El id del cliente no puede ser 0 o menor.");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, SessionStatus status) {
		if (id > 0) {
			Cliente cliente = clienteService.findById(id);
			clienteService.delete(id);

			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
			File archivo = rootPath.toFile();

			if (archivo.exists() && archivo.canRead()) {
				if (archivo.delete()) {
					flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito.");
				}
			}
		}

		status.setComplete();
		flash.addFlashAttribute("success", "Cliente borrado con exito.");
		return "redirect:/listar";
	}

}
