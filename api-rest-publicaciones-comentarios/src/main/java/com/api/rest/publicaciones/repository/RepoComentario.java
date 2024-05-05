package com.api.rest.publicaciones.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.rest.publicaciones.entity.Comentario;

public interface RepoComentario extends JpaRepository<Comentario, Long>
{
	Page<Comentario> findByPublicacionId(Long publicacionId,Pageable pageable);
	
	Optional<Comentario> findByIdAndPublicacionId(Long comentarioId, Long publicacionId);
}
