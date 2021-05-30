package com.devsuperior.bds02.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.services.exception.DatabaseException;
import com.devsuperior.bds02.services.exception.ResourceNotFoundException;

@Service
public class CityService {

	@Autowired
	private CityRepository repository;

	@Transactional(readOnly = true)
	public List<CityDTO> findAll() {
		List<City> list = repository.findAll(Sort.by("name"));
		return list.stream().map(x -> new CityDTO(x)).collect(Collectors.toList());
	}

	@Transactional
	public CityDTO update(Long id, CityDTO dto) {

		try {
			City entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CityDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("erro ao atualizar dados" + e.getMessage());

		}
	}

	public void delete(Long id) {

		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("id não existe " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Violação de integridade no banco");
		}
	}
}
