package com.egg.libreria.controladores;

import java.util.List;

import com.egg.libreria.entidades.Editorial;
import com.egg.libreria.servicios.EditorialServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@RequestMapping("/editorial")
public class EditorialControlador {
    
    @Autowired
    private EditorialServicio editorialServicio;
    
    @GetMapping("/registro-editorial")
    public String registro() {
        return "registro-editorial";
    }

    @PostMapping("/registro-editorial")
    public String guardar(ModelMap modelo, @RequestParam String nombre) {

        try {
            editorialServicio.crear(nombre);

            modelo.put("exito", "Registro realizado con éxito!");

            return "registro-editorial";
        } catch (Exception e) {
            modelo.put("error", "Faltó algún dato!");

            return "registro-editorial";
        }
    }

    @GetMapping("/lista-editoriales")
    public String lista(ModelMap modelo) {

        List<Editorial> editoriales = editorialServicio.listarTodos();
        
        modelo.addAttribute("editoriales", editoriales);
        
        return "lista-editoriales";
    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {
        try {
            editorialServicio.habilitar(id);

            return "redirect:/editorial/lista-editoriales";
        } catch (Exception e) {            
            return "redirect:/";
        }
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {
        try {
            editorialServicio.deshabilitar(id);

            return "redirect:/editorial/lista-editoriales";
        } catch (Exception e) {            
            return "redirect:/";
        }
    }

    @GetMapping("/registro-editorialmodif/{id}")
    public String formularioModif(@PathVariable String id, ModelMap modelo) {

        modelo.put("editorial", editorialServicio.getOne(id));

        return "registro-editorialmodif";
    }

    @PostMapping("/registro-editorialmodif/{id}")
	public String formularioModif(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {
		
		try {
			editorialServicio.modificar(id, nombre);
            
			return "redirect:/editorial/lista-editoriales";
		} catch (Exception e) {
			modelo.put("error", "Falto algun dato");   
			return "redirect:/editorial/registro-editorialmodif";
		}
	}
}
