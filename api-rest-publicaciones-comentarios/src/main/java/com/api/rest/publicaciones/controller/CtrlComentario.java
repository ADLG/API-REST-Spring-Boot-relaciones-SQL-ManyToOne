package com.api.rest.publicaciones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.rest.publicaciones.entity.Comentario;
import com.api.rest.publicaciones.exceptions.ResourceNotFoundException;
import com.api.rest.publicaciones.repository.RepoComentario;
import com.api.rest.publicaciones.repository.RepoPublicacion;

import jakarta.validation.Valid;

@RestController
public class CtrlComentario
{
	@Autowired
	private RepoComentario repoComentario;

	@Autowired
	private RepoPublicacion repoPublicacion;

	@GetMapping("/publicaciones/{publicacionId}/comentarios")
	public Page<Comentario> listarComentariosPorPublicacion(@PathVariable(value = "publicacionId") Long publicacionId, Pageable pageable)
	{
		return repoComentario.findByPublicacionId(publicacionId,pageable);
	}

	@PostMapping("/publicaciones/{publicacionId}/comentarios")
	public Comentario guardarComentario(@PathVariable(value = "publicacionId") Long publicacionId, @Valid @RequestBody Comentario comentario)
	{
		return repoPublicacion.findById(publicacionId).map(publicacion ->
			{
				comentario.setPublicacion(publicacion);
				return repoComentario.save(comentario);
			}).orElseThrow(() -> new ResourceNotFoundException("Publicacion con el ID : "+ publicacionId + " no encontrada"));
	}

	@PutMapping("/publicaciones/{publicacionId}/comentarios/{comentarioId}")
	public Comentario actualizarComentario(@PathVariable(value = "publicacionId") Long publicacionId,@PathVariable(value = "comentarioId") Long comentarioId, @Valid @RequestBody Comentario comentarioRequest)
	{
		if (!repoPublicacion.existsById(publicacionId))
		{
			throw new ResourceNotFoundException("Publicacion con el ID : "+ publicacionId + " no encontrada");
		}

		return repoComentario.findById(comentarioId).map(comentario -> 
			{
				comentario.setTexto(comentarioRequest.getTexto());
				return repoComentario.save(comentario);
			}).orElseThrow(() -> new ResourceNotFoundException("Comentario con el ID : "+ comentarioId + " no encontrado"));
	}

	@DeleteMapping("/publicaciones/{publicacionId}/comentarios/{comentarioId}")
	public ResponseEntity<?> eliminarComentario(@PathVariable(value = "publicacionId") Long publicacionId,@PathVariable(value = "comentarioId") Long comentarioId)
	{
		return repoComentario.findByIdAndPublicacionId(comentarioId,publicacionId).map(comentario ->
		{
			repoComentario.delete(comentario);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("Comentario con el ID : "+ comentarioId + " no encontrado y publicacion con el ID : " + publicacionId + " no encontrada"));
	}
}
