package com.egg.libreria.repositorios;

import com.egg.libreria.entidades.Foto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto, String> {

}
