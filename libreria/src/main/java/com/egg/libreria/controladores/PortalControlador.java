package com.egg.libreria.controladores;

import com.egg.libreria.errores.ErrorServicio;
import com.egg.libreria.servicios.UsuarioServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
            ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o clave incorrectos");            
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente.");
        }
        return "index";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }
    
    @PostMapping("/registro")
    public String registrar(ModelMap modelo, @RequestParam String nombre,
            @RequestParam String apellido, @RequestParam String email,
            @RequestParam String clave1, @RequestParam String clave2) {

        try {
            usuarioServicio.registrar(null, nombre, apellido, email, clave1, clave2);

            modelo.put("exito", "Bienvenido a La Librería");

            return "index";

        } catch (ErrorServicio ex) {
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", email);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);

            modelo.put("error", "El usuario no se puedo registrar con éxito");

            return "index";
        }

    }

}
