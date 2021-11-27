package com.egg.libreria.controladores;

import javax.servlet.http.HttpSession;

import com.egg.libreria.entidades.Usuario;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap model) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = usuarioServicio.buscarPorId(id);
            model.addAttribute("perfil", usuario);
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }
        return "perfil.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @PostMapping("/actualizar-perfil")
    public String registrar(ModelMap modelo, HttpSession session, @RequestParam String id, @RequestParam long documento,
            @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail,
            MultipartFile archivo, @RequestParam String telefono, @RequestParam String clave1,
            @RequestParam String clave2) {

        Usuario usuario = null;
        try {

            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }

            usuario = usuarioServicio.buscarPorId(id);
            usuarioServicio.modificar(archivo, id, documento, nombre, apellido, mail, telefono, clave1, clave2);
            session.setAttribute("usuariosession", usuario);

            return "redirect:/inicio";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", usuario);

            return "perfil.html";
        }

    }
}
