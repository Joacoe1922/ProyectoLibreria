package com.egg.libreria.controladores;

import java.util.List;

import com.egg.libreria.entidades.Autor;
import com.egg.libreria.servicios.AutorServicio;

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
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;
    
    @GetMapping("/registro-autor")
    public String registro() {
        return "registro-autor";
    }

    @PostMapping("/registro-autor")
    public String guardar(ModelMap modelo, @RequestParam String nombre) {

        try {
            autorServicio.crear(nombre);

            modelo.put("exito", "Registro realizado con éxito!");

            return "registro-autor";
        } catch (Exception e) {
            modelo.put("error", "Faltó algún dato!");

            return "registro-autor";
        }
    }

    @GetMapping("/lista-autores")
    public String lista(ModelMap modelo) {

        List<Autor> autores = autorServicio.listarTodos();
        
        modelo.addAttribute("autores", autores);
        
        return "lista-autores";
    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {
        try {
            autorServicio.habilitar(id);

            return "redirect:/autor/lista-autores";
        } catch (Exception e) {            
            return "redirect:/";
        }
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {
        try {
            autorServicio.deshabilitar(id);

            return "redirect:/autor/lista-autores";
        } catch (Exception e) {            
            return "redirect:/";
        }
    }

    @GetMapping("/registro-autormodif/{id}")
    public String formularioModif(@PathVariable String id, ModelMap modelo) {

        modelo.put("autor", autorServicio.getOne(id));

        return "registro-autormodif";
    }

    @PostMapping("/registro-autormodif/{id}")
	public String formularioModif(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {
		
		try {
			autorServicio.modificar(id, nombre);
            
			return "redirect:/autor/lista-autores";
		} catch (Exception e) {
			modelo.put("error", "Falto algun dato");   
			return "redirect:/autor/registro-autormodif";
		}
	}
}
