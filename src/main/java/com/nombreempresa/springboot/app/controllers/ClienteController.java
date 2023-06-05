package com.nombreempresa.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
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
import com.nombreempresa.springboot.app.models.service.IUploadFileService;
import com.nombreempresa.springboot.app.util.paginator.PageRender;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("cliente") // Guardo la entidad de clientes en la sesion hasta que acaba de guardarlo o
								// editarlo
public class ClienteController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;

	@Autowired
	private MessageSource messageSource;

	@Secured("ROLE_USER")
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource resource = null;
		try {
			resource = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@Secured("ROLE_USER")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash,
			Locale locale) {
		Cliente cliente = clienteService.findById(id);
		if (cliente == null) {
			flash.addAttribute("error", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Detalle de cliente: " + cliente.getNombre());

		return "ver";
	}

	@RequestMapping(value = { "/listar", "/" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request, Locale locale) {

		if (authentication != null) {
			logger.info("Hola usuario autentificado, tu username es:  " + authentication.getName());
		}

		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// De esta manera se puede obtener el auth en cualquiier parte

		// Otra manera de obtener el rol y checkearlo
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
				"");
		/*
		 * if (securityContext.isUserInRole("ROLE_ADMIN")) {
		 * logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola " +
		 * authentication.getName() + " tienes rol de admin "); } else {
		 * logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola " +
		 * authentication.getName() + " no tienes rol de admin "); }
		 * ///////////////////////////////////////////////////////// // Tambien se puede
		 * comparar usando el objeto HttpServletRequest if
		 * (request.isUserInRole("ROLE_ADMIN")) {
		 * logger.info("Forma usando HttpServletRequest: Hola " +
		 * authentication.getName() + " tienes rol de admin "); } else { logger.info(
		 * "Forma usando HttpServletRequest: Hola " + authentication.getName() +
		 * " no tienes rol de admin "); }
		 * 
		 * ///////////////////////////////////////////////////////// if
		 * (hasRole("ROLE_ADMIN")) {
		 * logger.info("El usuario tiene accesso de administrador"); } else {
		 * logger.info("El usuario no tiene accesso de administrador"); }
		 */

		Pageable pageable = PageRequest.of(page, 5);

		Page<Cliente> clientes = clienteService.findAll(pageable);

		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("titulo", messageSource.getMessage("text.interfaz.listadoclientes", null, locale));
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model, Locale locale) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.interfaz.listadoclientes", null, locale));
		return "form";
	}

	@Secured("ROLE_ADMIN")
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
				uploadFileService.delete(cliente.getFoto());
			}

//			Path directorioRecursos = Paths.get("src//main//resources//static/uploads");    Ruta interna del proyecto
//			String rootPath = directorioRecursos.toFile().getAbsolutePath();

//			String rootPath = "D://Desarrollo//uploads"; // Ruta directorio externo al proyecto. Ruta en maquina que
			// ejecuta el servicio

			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Se ha subido correctamente la foto: " + uniqueFilename);

			cliente.setFoto(uniqueFilename);

		}

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", flashMessage);
		return "redirect:listar";
	}

	@Secured("ROLE_ADMIN")
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

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, SessionStatus status) {
		if (id > 0) {
			Cliente cliente = clienteService.findById(id);
			clienteService.delete(id);

			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito.");
			}

		}

		status.setComplete();
		flash.addFlashAttribute("success", "Cliente borrado con exito.");
		return "redirect:/listar";
	}

	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();

		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		// return authorities.contains(new SimpleGrantedAuthority(role)); // Metodo
		// simple para obtener el usuario con el
		// rol especificado

		for (GrantedAuthority authority : authorities) {
			if (role.equals(authority.getAuthority())) {
				logger.info("Hola usuario " + auth.getName() + " tu rol es: " + authority.getAuthority()); // La clase
																											// GrantedAuthority
																											// da
																											// informacion
																											// de los
																											// roles de
																											// usuario
				return true;
			}
		}

		return false;
	}
}
