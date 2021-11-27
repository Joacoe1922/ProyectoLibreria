package com.egg.libreria.servicios;

import java.util.List;
import java.util.Optional;

import com.egg.libreria.entidades.Autor;
import com.egg.libreria.entidades.Editorial;
import com.egg.libreria.entidades.Libro;
import com.egg.libreria.errores.ErrorServicio;
import com.egg.libreria.repositorios.AutorRepositorio;
import com.egg.libreria.repositorios.EditorialRepositorio;
import com.egg.libreria.repositorios.LibroRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional
    public void crear(String isbn, String titulo, int anio, int ejemplares, String autor, String editorial)
            throws ErrorServicio {
        validar(isbn, titulo, anio, ejemplares, autor, editorial);

        Libro libro = new Libro();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(0);
        libro.setEjemplaresRestantes(libro.getEjemplares() - libro.getEjemplaresPrestados());
        libro.setAlta(true);

        Autor a = autorRepositorio.buscarPorNombre(autor);
        Editorial e = editorialRepositorio.buscarPorNombre(editorial);
        libro.setAutor(a);
        libro.setEditorial(e);

        libroRepositorio.save(libro);
    }

    @Transactional
    public void modificar(String id, String isbn, String titulo, int anio, int ejemplares, String autor,
            String editorial) throws ErrorServicio {
        validar(isbn, titulo, anio, ejemplares, autor, editorial);

        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(0);
            libro.setEjemplaresRestantes(libro.getEjemplares() - libro.getEjemplaresPrestados());

            Autor a = autorRepositorio.buscarPorNombre(autor);
            Editorial e = editorialRepositorio.buscarPorNombre(editorial);
            libro.setAutor(a);
            libro.setEditorial(e);

            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontró el libro solicitado");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(false);

            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontró el libro solicitado");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(true);

            libroRepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontró el libro solicitado");
        }
    }

    @Transactional(readOnly = true)
    public List<Libro> listarTodos() {
        return libroRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    //Este cambio es para que me busque libros que "contengan" lo que ingrese al buscador por parte del nombre
    public List<Libro> buscarPorNombre(String titulo) {
        titulo = "%"+titulo+"%";
        return libroRepositorio.buscarPorNombre(titulo);
    }

    @Transactional(readOnly = true)
    public Libro buscarSolotitulo(String titulo) {
        return libroRepositorio.buscarSolotitulo(titulo);
    }

    @Transactional(readOnly = true)
    public Libro getOne(String id) {
        return libroRepositorio.getById(id);
    }

    public void validar(String isbn, String titulo, int anio, int ejemplares, String autor, String editorial)
            throws ErrorServicio {
        if (isbn.length() != 13 || isbn.isEmpty() || isbn == null) {
            throw new ErrorServicio("El ISBN debe contener 13 cifras");
        }

        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("El título no puede ser nulo o estar vacío");
        }

        if (anio == 0) {
            throw new ErrorServicio("El año no puede ser nulo");
        }

        if (ejemplares == 0) {
            throw new ErrorServicio("Los ejemplares no pueden ser nulos");
        }

        if (autor == null || autor.isEmpty()) {
            throw new ErrorServicio("El autor no puede ser nulo o estar vacío");
        }

        if (editorial == null || editorial.isEmpty()) {
            throw new ErrorServicio("El editorial no puede ser nulo o estar vacío");
        }
    }
}
