package com.egg.libreria.repositorios;

import java.util.List;

import com.egg.libreria.entidades.Prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String> {

    @Query("SELECT p FROM Prestamo p ORDER BY fecha_prestamo DESC")
    List<Prestamo> buscarTodosOrdenadosPorFechaDePrestamo();

}
