package com.nirlir.service;

import com.nirlir.domain.Technician;
import com.nirlir.repository.TechnicianRepository;
import com.nirlir.service.dto.TechnicianDTO;
import com.nirlir.service.mapper.TechnicianMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Technician}.
 */
@Service
@Transactional
public class TechnicianService {

    private final Logger log = LoggerFactory.getLogger(TechnicianService.class);

    private final TechnicianRepository technicianRepository;

    private final TechnicianMapper technicianMapper;

    public TechnicianService(TechnicianRepository technicianRepository, TechnicianMapper technicianMapper) {
        this.technicianRepository = technicianRepository;
        this.technicianMapper = technicianMapper;
    }

    /**
     * Save a technician.
     *
     * @param technicianDTO the entity to save.
     * @return the persisted entity.
     */
    public TechnicianDTO save(TechnicianDTO technicianDTO) {
        log.debug("Request to save Technician : {}", technicianDTO);
        Technician technician = technicianMapper.toEntity(technicianDTO);
        technician = technicianRepository.save(technician);
        return technicianMapper.toDto(technician);
    }

    /**
     * Update a technician.
     *
     * @param technicianDTO the entity to save.
     * @return the persisted entity.
     */
    public TechnicianDTO update(TechnicianDTO technicianDTO) {
        log.debug("Request to update Technician : {}", technicianDTO);
        Technician technician = technicianMapper.toEntity(technicianDTO);
        technician = technicianRepository.save(technician);
        return technicianMapper.toDto(technician);
    }

    /**
     * Partially update a technician.
     *
     * @param technicianDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TechnicianDTO> partialUpdate(TechnicianDTO technicianDTO) {
        log.debug("Request to partially update Technician : {}", technicianDTO);

        return technicianRepository
            .findById(technicianDTO.getId())
            .map(existingTechnician -> {
                technicianMapper.partialUpdate(existingTechnician, technicianDTO);

                return existingTechnician;
            })
            .map(technicianRepository::save)
            .map(technicianMapper::toDto);
    }

    /**
     * Get all the technicians.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TechnicianDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Technicians");
        return technicianRepository.findAll(pageable).map(technicianMapper::toDto);
    }

    /**
     * Get one technician by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TechnicianDTO> findOne(Long id) {
        log.debug("Request to get Technician : {}", id);
        return technicianRepository.findById(id).map(technicianMapper::toDto);
    }

    /**
     * Delete the technician by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Technician : {}", id);
        technicianRepository.deleteById(id);
    }
}
