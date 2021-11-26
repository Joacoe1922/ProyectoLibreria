package com.egg.libreria.servicios;

import java.util.Date;
import java.util.Optional;

import com.egg.libreria.entidades.Libro;
import com.egg.libreria.entidades.Prestamo;
import com.egg.libreria.entidades.Usuario;
import com.egg.libreria.errores.ErrorServicio;
import com.egg.libreria.repositorios.LibroRepositorio;
import com.egg.libreria.repositorios.PrestamoRepositorio;
import com.egg.libreria.repositorios.UsuarioRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public void crear(String id, String titulo, int ejemplares) throws ErrorServicio {

        Libro l = libroRepositorio.buscarSolotitulo(titulo);

        if (l.getAlta() && l.getEjemplaresRestantes() >= ejemplares) {
            Prestamo prestamo = new Prestamo();

            prestamo.setFechaPrestamo(new Date());
            prestamo.setFechaDevolucion(null);
            prestamo.setAlta(true);

            l.setEjemplaresPrestados(l.getEjemplaresPrestados() + ejemplares);
            l.setEjemplaresRestantes(l.getEjemplares() - l.getEjemplaresPrestados());

            prestamo.setLibro(l);

            Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
            if (respuesta.isPresent()) {
                Usuario u = respuesta.get();
                prestamo.setUsuario(u);
            } else {
                throw new ErrorServicio("No se encontró el usuario solicitado");
            }

            prestamoRepositorio.save(prestamo);
        } else {
            throw new ErrorServicio("El libro está dado de baja o no quedan ejemplares");
        }

    }

}
