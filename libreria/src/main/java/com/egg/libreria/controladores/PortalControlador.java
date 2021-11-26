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
    public String index() {
        return "index";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
            ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o clave incorrectos");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente.");
        }
        return "login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(ModelMap modelo, @RequestParam long documento, @RequestParam String nombre,
            @RequestParam String apellido, @RequestParam String mail, @RequestParam String telefono,
            @RequestParam String clave1, @RequestParam String clave2) {

        try {
            usuarioServicio.registrar(documento, nombre, apellido, mail, telefono, clave1, clave2);

            modelo.put("exito", "Bienvenido a La Librería");
            //modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria");
            return "registro";

        } catch (ErrorServicio ex) {
            modelo.put("documento", documento);
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("telefono", telefono);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);

            modelo.put("error", "El usuario no se puedo registrar con éxito");

            return "registro";
        }

    }

}
