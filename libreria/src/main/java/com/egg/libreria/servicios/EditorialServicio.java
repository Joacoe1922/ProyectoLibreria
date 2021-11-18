package com.egg.libreria.servicios;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.egg.libreria.entidades.Editorial;
import com.egg.libreria.errores.ErrorServicio;
import com.egg.libreria.repositorios.EditorialRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crear(String nombre) throws ErrorServicio {
        validar(nombre);

        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(true);

        editorialRepositorio.save(editorial);
    }

    @Transactional
    public void modificar(String id, String nombre) throws ErrorServicio {
        validar(nombre);

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
        } else {
            throw new ErrorServicio("No se encontró la editorial solicitada");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(false);

            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontró la editorial solicitada");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(true);

            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontró la editorial solicitada");
        }
    }

    @Transactional
    public List<Editorial> listarTodos() {
        return editorialRepositorio.findAll();
    }

    @Transactional
    public Editorial getOne(String id) {
        return editorialRepositorio.getById(id);
    }

    public void validar(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo o estar vacío");
        }
    }
}
