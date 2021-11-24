package com.egg.libreria.servicios;

import java.util.Date;

import com.egg.libreria.entidades.Libro;
import com.egg.libreria.entidades.Prestamo;
import com.egg.libreria.errores.ErrorServicio;
import com.egg.libreria.repositorios.LibroRepositorio;
import com.egg.libreria.repositorios.PrestamoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;

    @Autowired
    private LibroRepositorio libroRepositorio;
    
    public void crear(String libro, int ejemplares_prestados) throws ErrorServicio {


        Prestamo prestamo = new Prestamo();

        prestamo.setFechaPrestamo(new Date());
        prestamo.setFechaDevolucion(new Date());
        prestamo.setAlta(true);

        Libro l = libroRepositorio.buscarSolotitulo(libro);
        l.setEjemplaresPrestados(l.getEjemplaresPrestados() + ejemplares_prestados);
        l.setEjemplaresRestantes(l.getEjemplares() - l.getEjemplaresPrestados());

        prestamo.setLibro(l);

        prestamo.setUsuario(null);
        

        prestamoRepositorio.save(prestamo);

    }

}
