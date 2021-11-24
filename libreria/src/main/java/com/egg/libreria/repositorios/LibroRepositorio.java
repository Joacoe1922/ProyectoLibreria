package com.egg.libreria.repositorios;

import java.util.List;

import com.egg.libreria.entidades.Libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query("SELECT l FROM Libro l WHERE l.titulo LIKE :titulo")
    List<Libro> buscarPorNombre(@Param("titulo") String titulo);

    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    Libro buscarSolotitulo(@Param("titulo") String titulo);
    
}
