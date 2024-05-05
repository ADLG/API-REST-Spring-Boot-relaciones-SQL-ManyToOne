package com.api.rest.publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.rest.publicaciones.entity.Publicacion;

public interface RepoPublicacion extends JpaRepository<Publicacion, Long>{

}
