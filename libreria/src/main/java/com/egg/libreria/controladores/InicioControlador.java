package com.egg.libreria.controladores;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
@RequestMapping("/inicio")
public class InicioControlador {

    
    @GetMapping("/libro")
    public String libro() {
        return "libro";
    }

    @GetMapping("/autor")
    public String autor() {
        return "autor";
    }

    @GetMapping("/editorial")
    public String editorial() {
        return "editorial";
    }
}
