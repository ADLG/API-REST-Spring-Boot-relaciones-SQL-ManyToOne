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

import com.api.rest.publicaciones.entity.Publicacion;
import com.api.rest.publicaciones.exceptions.ResourceNotFoundException;
import com.api.rest.publicaciones.repository.RepoPublicacion;

import jakarta.validation.Valid;

@RestController
public class CtrlPublicacion
{
	@Autowired
	private RepoPublicacion repoPublicacion;

	@GetMapping("/publicaciones")
	public Page<Publicacion> listarPublicaciones(Pageable pageable)
	{
		return repoPublicacion.findAll(pageable);
	}

	@PostMapping("/publicaciones")
	public Publicacion guardarPublicacion(@Valid @RequestBody Publicacion publicacion)
	{
		return repoPublicacion.save(publicacion);
	}

	@PutMapping("/publicaciones/{publicacionId}")
	public Publicacion actualizarPublicacion(@PathVariable Long publicacionId, @Valid @RequestBody Publicacion publicacionRequest)
	{
		return repoPublicacion.findById(publicacionId).map(publicacion -> 
			{
				publicacion.setTitulo(publicacionRequest.getTitulo());
				publicacion.setDescripcion(publicacionRequest.getDescripcion());
				publicacion.setContenido(publicacionRequest.getContenido());
				return repoPublicacion.save(publicacion);
			}).orElseThrow(() -> new ResourceNotFoundException("Publicacion con el ID : "+ publicacionId + " no encontrada"));
	}

	@DeleteMapping("/publicaciones/{publicacionId}")
	public ResponseEntity<?> eliminarPublicacion(@PathVariable Long publicacionId)
	{
		return repoPublicacion.findById(publicacionId).map(publicacion -> 
		{
			repoPublicacion.delete(publicacion);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("Publicacion con el ID : "+ publicacionId + " no encontrada"));
	}

}
