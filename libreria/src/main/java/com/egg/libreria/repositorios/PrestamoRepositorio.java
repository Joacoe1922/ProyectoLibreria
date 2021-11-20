package com.egg.libreria.repositorios;

import com.egg.libreria.entidades.Prestamo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String> {

}
