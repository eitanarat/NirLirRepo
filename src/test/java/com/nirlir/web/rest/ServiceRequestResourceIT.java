package com.nirlir.web.rest;

import static com.nirlir.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nirlir.IntegrationTest;
import com.nirlir.domain.Customer;
import com.nirlir.domain.ServiceRequest;
import com.nirlir.domain.Technician;
import com.nirlir.domain.enumeration.ServiceRequestStatus;
import com.nirlir.domain.enumeration.ServiceRequestType;
import com.nirlir.repository.ServiceRequestRepository;
import com.nirlir.service.ServiceRequestService;
import com.nirlir.service.criteria.ServiceRequestCriteria;
import com.nirlir.service.dto.ServiceRequestDTO;
import com.nirlir.service.mapper.ServiceRequestMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ServiceRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServiceRequestResourceIT {

    private static final ServiceRequestType DEFAULT_TYPE = ServiceRequestType.INSTALLATION;
    private static final ServiceRequestType UPDATED_TYPE = ServiceRequestType.REPAIR;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ServiceRequestStatus DEFAULT_STATUS = ServiceRequestStatus.SCHEDULED;
    private static final ServiceRequestStatus UPDATED_STATUS = ServiceRequestStatus.CANCELED;

    private static final String ENTITY_API_URL = "/api/service-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private ServiceRequestRepository serviceRequestRepositoryMock;

    @Autowired
    private ServiceRequestMapper serviceRequestMapper;

    @Mock
    private ServiceRequestService serviceRequestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceRequestMockMvc;

    private ServiceRequest serviceRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceRequest createEntity(EntityManager em) {
        ServiceRequest serviceRequest = new ServiceRequest()
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS);
        return serviceRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceRequest createUpdatedEntity(EntityManager em) {
        ServiceRequest serviceRequest = new ServiceRequest()
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);
        return serviceRequest;
    }

    @BeforeEach
    public void initTest() {
        serviceRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createServiceRequest() throws Exception {
        int databaseSizeBeforeCreate = serviceRequestRepository.findAll().size();
        // Create the ServiceRequest
        ServiceRequestDTO serviceRequestDTO = serviceRequestMapper.toDto(serviceRequest);
        restServiceRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceRequestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceRequest testServiceRequest = serviceRequestList.get(serviceRequestList.size() - 1);
        assertThat(testServiceRequest.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testServiceRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testServiceRequest.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testServiceRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createServiceRequestWithExistingId() throws Exception {
        // Create the ServiceRequest with an existing ID
        serviceRequest.setId(1L);
        ServiceRequestDTO serviceRequestDTO = serviceRequestMapper.toDto(serviceRequest);

        int databaseSizeBeforeCreate = serviceRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceRequestRepository.findAll().size();
        // set the field null
        serviceRequest.setDate(null);

        // Create the ServiceRequest, which fails.
        ServiceRequestDTO serviceRequestDTO = serviceRequestMapper.toDto(serviceRequest);

        restServiceRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceRequestDTO))
            )
            .andExpect(status().isBadRequest());

        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceRequests() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList
        restServiceRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(serviceRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(serviceRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiceRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(serviceRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServiceRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(serviceRequestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getServiceRequest() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get the serviceRequest
        restServiceRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceRequest.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getServiceRequestsByIdFiltering() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        Long id = serviceRequest.getId();

        defaultServiceRequestShouldBeFound("id.equals=" + id);
        defaultServiceRequestShouldNotBeFound("id.notEquals=" + id);

        defaultServiceRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultServiceRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultServiceRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultServiceRequestShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where type equals to DEFAULT_TYPE
        defaultServiceRequestShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the serviceRequestList where type equals to UPDATED_TYPE
        defaultServiceRequestShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultServiceRequestShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the serviceRequestList where type equals to UPDATED_TYPE
        defaultServiceRequestShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where type is not null
        defaultServiceRequestShouldBeFound("type.specified=true");

        // Get all the serviceRequestList where type is null
        defaultServiceRequestShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where description equals to DEFAULT_DESCRIPTION
        defaultServiceRequestShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the serviceRequestList where description equals to UPDATED_DESCRIPTION
        defaultServiceRequestShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultServiceRequestShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the serviceRequestList where description equals to UPDATED_DESCRIPTION
        defaultServiceRequestShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where description is not null
        defaultServiceRequestShouldBeFound("description.specified=true");

        // Get all the serviceRequestList where description is null
        defaultServiceRequestShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where description contains DEFAULT_DESCRIPTION
        defaultServiceRequestShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the serviceRequestList where description contains UPDATED_DESCRIPTION
        defaultServiceRequestShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where description does not contain DEFAULT_DESCRIPTION
        defaultServiceRequestShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the serviceRequestList where description does not contain UPDATED_DESCRIPTION
        defaultServiceRequestShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where date equals to DEFAULT_DATE
        defaultServiceRequestShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the serviceRequestList where date equals to UPDATED_DATE
        defaultServiceRequestShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where date in DEFAULT_DATE or UPDATED_DATE
        defaultServiceRequestShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the serviceRequestList where date equals to UPDATED_DATE
        defaultServiceRequestShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where date is not null
        defaultServiceRequestShouldBeFound("date.specified=true");

        // Get all the serviceRequestList where date is null
        defaultServiceRequestShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where date is greater than or equal to DEFAULT_DATE
        defaultServiceRequestShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the serviceRequestList where date is greater than or equal to UPDATED_DATE
        defaultServiceRequestShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where date is less than or equal to DEFAULT_DATE
        defaultServiceRequestShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the serviceRequestList where date is less than or equal to SMALLER_DATE
        defaultServiceRequestShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where date is less than DEFAULT_DATE
        defaultServiceRequestShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the serviceRequestList where date is less than UPDATED_DATE
        defaultServiceRequestShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where date is greater than DEFAULT_DATE
        defaultServiceRequestShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the serviceRequestList where date is greater than SMALLER_DATE
        defaultServiceRequestShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where status equals to DEFAULT_STATUS
        defaultServiceRequestShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the serviceRequestList where status equals to UPDATED_STATUS
        defaultServiceRequestShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultServiceRequestShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the serviceRequestList where status equals to UPDATED_STATUS
        defaultServiceRequestShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllServiceRequestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        // Get all the serviceRequestList where status is not null
        defaultServiceRequestShouldBeFound("status.specified=true");

        // Get all the serviceRequestList where status is null
        defaultServiceRequestShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceRequestsByTechnicianIsEqualToSomething() throws Exception {
        Technician technician;
        if (TestUtil.findAll(em, Technician.class).isEmpty()) {
            serviceRequestRepository.saveAndFlush(serviceRequest);
            technician = TechnicianResourceIT.createEntity(em);
        } else {
            technician = TestUtil.findAll(em, Technician.class).get(0);
        }
        em.persist(technician);
        em.flush();
        serviceRequest.addTechnician(technician);
        serviceRequestRepository.saveAndFlush(serviceRequest);
        Long technicianId = technician.getId();

        // Get all the serviceRequestList where technician equals to technicianId
        defaultServiceRequestShouldBeFound("technicianId.equals=" + technicianId);

        // Get all the serviceRequestList where technician equals to (technicianId + 1)
        defaultServiceRequestShouldNotBeFound("technicianId.equals=" + (technicianId + 1));
    }

    @Test
    @Transactional
    void getAllServiceRequestsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            serviceRequestRepository.saveAndFlush(serviceRequest);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        serviceRequest.addCustomer(customer);
        serviceRequestRepository.saveAndFlush(serviceRequest);
        Long customerId = customer.getId();

        // Get all the serviceRequestList where customer equals to customerId
        defaultServiceRequestShouldBeFound("customerId.equals=" + customerId);

        // Get all the serviceRequestList where customer equals to (customerId + 1)
        defaultServiceRequestShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServiceRequestShouldBeFound(String filter) throws Exception {
        restServiceRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restServiceRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServiceRequestShouldNotBeFound(String filter) throws Exception {
        restServiceRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServiceRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServiceRequest() throws Exception {
        // Get the serviceRequest
        restServiceRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceRequest() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().size();

        // Update the serviceRequest
        ServiceRequest updatedServiceRequest = serviceRequestRepository.findById(serviceRequest.getId()).get();
        // Disconnect from session so that the updates on updatedServiceRequest are not directly saved in db
        em.detach(updatedServiceRequest);
        updatedServiceRequest.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).date(UPDATED_DATE).status(UPDATED_STATUS);
        ServiceRequestDTO serviceRequestDTO = serviceRequestMapper.toDto(updatedServiceRequest);

        restServiceRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
        ServiceRequest testServiceRequest = serviceRequestList.get(serviceRequestList.size() - 1);
        assertThat(testServiceRequest.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testServiceRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServiceRequest.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testServiceRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().size();
        serviceRequest.setId(count.incrementAndGet());

        // Create the ServiceRequest
        ServiceRequestDTO serviceRequestDTO = serviceRequestMapper.toDto(serviceRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().size();
        serviceRequest.setId(count.incrementAndGet());

        // Create the ServiceRequest
        ServiceRequestDTO serviceRequestDTO = serviceRequestMapper.toDto(serviceRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(serviceRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().size();
        serviceRequest.setId(count.incrementAndGet());

        // Create the ServiceRequest
        ServiceRequestDTO serviceRequestDTO = serviceRequestMapper.toDto(serviceRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceRequestMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(serviceRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceRequestWithPatch() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().size();

        // Update the serviceRequest using partial update
        ServiceRequest partialUpdatedServiceRequest = new ServiceRequest();
        partialUpdatedServiceRequest.setId(serviceRequest.getId());

        restServiceRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceRequest))
            )
            .andExpect(status().isOk());

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
        ServiceRequest testServiceRequest = serviceRequestList.get(serviceRequestList.size() - 1);
        assertThat(testServiceRequest.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testServiceRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testServiceRequest.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testServiceRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateServiceRequestWithPatch() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().size();

        // Update the serviceRequest using partial update
        ServiceRequest partialUpdatedServiceRequest = new ServiceRequest();
        partialUpdatedServiceRequest.setId(serviceRequest.getId());

        partialUpdatedServiceRequest.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).date(UPDATED_DATE).status(UPDATED_STATUS);

        restServiceRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServiceRequest))
            )
            .andExpect(status().isOk());

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
        ServiceRequest testServiceRequest = serviceRequestList.get(serviceRequestList.size() - 1);
        assertThat(testServiceRequest.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testServiceRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testServiceRequest.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testServiceRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().size();
        serviceRequest.setId(count.incrementAndGet());

        // Create the ServiceRequest
        ServiceRequestDTO serviceRequestDTO = serviceRequestMapper.toDto(serviceRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().size();
        serviceRequest.setId(count.incrementAndGet());

        // Create the ServiceRequest
        ServiceRequestDTO serviceRequestDTO = serviceRequestMapper.toDto(serviceRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceRequest() throws Exception {
        int databaseSizeBeforeUpdate = serviceRequestRepository.findAll().size();
        serviceRequest.setId(count.incrementAndGet());

        // Create the ServiceRequest
        ServiceRequestDTO serviceRequestDTO = serviceRequestMapper.toDto(serviceRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(serviceRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceRequest in the database
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceRequest() throws Exception {
        // Initialize the database
        serviceRequestRepository.saveAndFlush(serviceRequest);

        int databaseSizeBeforeDelete = serviceRequestRepository.findAll().size();

        // Delete the serviceRequest
        restServiceRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ServiceRequest> serviceRequestList = serviceRequestRepository.findAll();
        assertThat(serviceRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
