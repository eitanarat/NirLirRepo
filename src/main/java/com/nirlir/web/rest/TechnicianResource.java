package com.nirlir.web.rest;

import com.nirlir.repository.TechnicianRepository;
import com.nirlir.service.TechnicianQueryService;
import com.nirlir.service.TechnicianService;
import com.nirlir.service.criteria.TechnicianCriteria;
import com.nirlir.service.dto.TechnicianDTO;
import com.nirlir.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nirlir.domain.Technician}.
 */
@RestController
@RequestMapping("/api")
public class TechnicianResource {

    private final Logger log = LoggerFactory.getLogger(TechnicianResource.class);

    private static final String ENTITY_NAME = "technician";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechnicianService technicianService;

    private final TechnicianRepository technicianRepository;

    private final TechnicianQueryService technicianQueryService;

    public TechnicianResource(
        TechnicianService technicianService,
        TechnicianRepository technicianRepository,
        TechnicianQueryService technicianQueryService
    ) {
        this.technicianService = technicianService;
        this.technicianRepository = technicianRepository;
        this.technicianQueryService = technicianQueryService;
    }

    /**
     * {@code POST  /technicians} : Create a new technician.
     *
     * @param technicianDTO the technicianDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new technicianDTO, or with status {@code 400 (Bad Request)} if the technician has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/technicians")
    public ResponseEntity<TechnicianDTO> createTechnician(@Valid @RequestBody TechnicianDTO technicianDTO) throws URISyntaxException {
        log.debug("REST request to save Technician : {}", technicianDTO);
        if (technicianDTO.getId() != null) {
            throw new BadRequestAlertException("A new technician cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TechnicianDTO result = technicianService.save(technicianDTO);
        return ResponseEntity
            .created(new URI("/api/technicians/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /technicians/:id} : Updates an existing technician.
     *
     * @param id the id of the technicianDTO to save.
     * @param technicianDTO the technicianDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technicianDTO,
     * or with status {@code 400 (Bad Request)} if the technicianDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the technicianDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/technicians/{id}")
    public ResponseEntity<TechnicianDTO> updateTechnician(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TechnicianDTO technicianDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Technician : {}, {}", id, technicianDTO);
        if (technicianDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technicianDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!technicianRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TechnicianDTO result = technicianService.update(technicianDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, technicianDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /technicians/:id} : Partial updates given fields of an existing technician, field will ignore if it is null
     *
     * @param id the id of the technicianDTO to save.
     * @param technicianDTO the technicianDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technicianDTO,
     * or with status {@code 400 (Bad Request)} if the technicianDTO is not valid,
     * or with status {@code 404 (Not Found)} if the technicianDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the technicianDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/technicians/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TechnicianDTO> partialUpdateTechnician(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TechnicianDTO technicianDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Technician partially : {}, {}", id, technicianDTO);
        if (technicianDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technicianDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!technicianRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TechnicianDTO> result = technicianService.partialUpdate(technicianDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, technicianDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /technicians} : get all the technicians.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of technicians in body.
     */
    @GetMapping("/technicians")
    public ResponseEntity<List<TechnicianDTO>> getAllTechnicians(
        TechnicianCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Technicians by criteria: {}", criteria);
        Page<TechnicianDTO> page = technicianQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /technicians/count} : count all the technicians.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/technicians/count")
    public ResponseEntity<Long> countTechnicians(TechnicianCriteria criteria) {
        log.debug("REST request to count Technicians by criteria: {}", criteria);
        return ResponseEntity.ok().body(technicianQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /technicians/:id} : get the "id" technician.
     *
     * @param id the id of the technicianDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the technicianDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/technicians/{id}")
    public ResponseEntity<TechnicianDTO> getTechnician(@PathVariable Long id) {
        log.debug("REST request to get Technician : {}", id);
        Optional<TechnicianDTO> technicianDTO = technicianService.findOne(id);
        return ResponseUtil.wrapOrNotFound(technicianDTO);
    }

    /**
     * {@code DELETE  /technicians/:id} : delete the "id" technician.
     *
     * @param id the id of the technicianDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/technicians/{id}")
    public ResponseEntity<Void> deleteTechnician(@PathVariable Long id) {
        log.debug("REST request to delete Technician : {}", id);
        technicianService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
