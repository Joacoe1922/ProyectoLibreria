package com.egg.libreria.servicios;

import java.util.Date;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private NotificacionServicio notificacionServicio;

    @Transactional
    public void prestar(String id, String titulo) throws ErrorServicio {

        Libro l = libroRepositorio.buscarSolotitulo(titulo);

        if (l.getAlta() && l.getEjemplaresRestantes() > 0) {
            Prestamo prestamo = new Prestamo();

            prestamo.setFechaPrestamo(new Date());
            prestamo.setFechaDevolucion(null);
            prestamo.setAlta(true);

            l.setEjemplaresPrestados(l.getEjemplaresPrestados() + 1);
            l.setEjemplaresRestantes(l.getEjemplares() - l.getEjemplaresPrestados());

            prestamo.setLibro(l);

            Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
            Usuario u = null;
            if (respuesta.isPresent()) {
                u = respuesta.get();
                prestamo.setUsuario(u);
            } else {
                throw new ErrorServicio("No se encontró el usuario solicitado");
            }

            prestamoRepositorio.save(prestamo);

            notificacionServicio.enviar("Préstamo realizado con éxito! - libro: " + prestamo.getLibro().getTitulo(), "Tu Librería - Préstamo", u.getMail());
        } else {
            throw new ErrorServicio("El libro está dado de baja o no quedan ejemplares");
        }
    }

    @Transactional
    public void devolver(String id) throws ErrorServicio {

        Optional<Prestamo> respuesta = prestamoRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();

            if (prestamo.getAlta()) {
                prestamo.setFechaDevolucion(new Date());
                prestamo.setAlta(false);

                prestamo.getLibro().setEjemplaresPrestados(prestamo.getLibro().getEjemplaresPrestados() - 1);
                prestamo.getLibro().setEjemplaresRestantes(prestamo.getLibro().getEjemplaresRestantes() + 1);

                prestamoRepositorio.save(prestamo);

                notificacionServicio.enviar("Préstamo devuelto con éxito!", "Tu Librería - Préstamo", prestamo.getUsuario().getMail());
            } else {
                throw new ErrorServicio("Ya se encuentra devuelto");
            }

        } else {
            throw new ErrorServicio("No se encontró el préstamo solicitado");
        }

    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarTodos() {
        return prestamoRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarTodosOrdenadosPorFechaDePrestamo() {
        return prestamoRepositorio.buscarTodosOrdenadosPorFechaDePrestamo();
    }

}
