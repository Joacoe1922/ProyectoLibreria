package com.egg.libreria.servicios;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.egg.libreria.entidades.Autor;
import com.egg.libreria.errores.ErrorServicio;
import com.egg.libreria.repositorios.AutorRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crear(String nombre) throws ErrorServicio {
        validar(nombre);

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(true);

        autorRepositorio.save(autor);
    }

    @Transactional
    public void modificar(String id, String nombre) throws ErrorServicio {
        validar(nombre);

        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
        } else {
            throw new ErrorServicio("No se encontró el autor solicitado");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(false);

            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontró el autor solicitado");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(true);

            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontró el autor solicitado");
        }
    }

    @Transactional
    public List<Autor> listarTodos() {
        return autorRepositorio.findAll();
    }

    @Transactional
    public Autor getOne(String id) {
        return autorRepositorio.getById(id);
    }

    public void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo o estar vacío");
        }
    }
}
