package com.m2i.sgpc.service;

import com.m2i.sgpc.domain.Personne;
import com.m2i.sgpc.domain.Production;
import com.m2i.sgpc.domain.User;
import com.m2i.sgpc.domain.enumeration.ETATPRODUCTION;
import com.m2i.sgpc.repository.PersonneRepository;
import com.m2i.sgpc.repository.ProductionRepository;
import com.m2i.sgpc.repository.UserRepository;
import com.m2i.sgpc.security.SecurityUtils;
import com.m2i.sgpc.service.dto.ProductionDTO;
import com.m2i.sgpc.service.mapper.ProductionMapper;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.m2i.sgpc.domain.Production}.
 */
@Service
@Transactional
public class ProductionService {

    private final Logger log = LoggerFactory.getLogger(ProductionService.class);

    private final ProductionRepository productionRepository;

    private final ProductionMapper productionMapper;

    private final MailService mailService;

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private UserRepository userRepository;

    public ProductionService(ProductionRepository productionRepository, ProductionMapper productionMapper, MailService mailService) {
        this.productionRepository = productionRepository;
        this.productionMapper = productionMapper;
        this.mailService = mailService;
    }

    /**
     * Save a production.
     *
     * @param productionDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductionDTO save(ProductionDTO productionDTO) {
        log.debug("Request to save Production : {}", productionDTO);
        Production production = productionMapper.toEntity(productionDTO);
        production.setEtat(ETATPRODUCTION.ATTENTE);
        production.setPersonne(personneRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()));
        production.setDateCreation(ZonedDateTime.now());
        production = productionRepository.save(production);
        return productionMapper.toDto(production);
    }

    /**
     * Update a production.
     *
     * @param productionDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductionDTO update(ProductionDTO productionDTO) {
        log.debug("Request to update Production : {}", productionDTO);
        Production production = productionMapper.toEntity(productionDTO);
        if (production.getFichierReception() != null) {
            production.setDateFin(LocalDate.now());
            production.setEtat(ETATPRODUCTION.COURRIER);
            production.setProducteur(personneRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().orElseThrow()));
            Personne destinataire = personneRepository.getReferenceById(production.getPersonne().getId());
            User user = userRepository.getReferenceById(destinataire.getUser().getId());
            mailService.sendEmail(
                user.getEmail(),
                "Production terminée",
                "Votre production a été terminée le " +
                production.getDateFin() +
                ". \n Le colisage vous sera transmis dans quelques jours." +
                "\n \n Cordialement M2i-SA ",
                false,
                false
            );
        } else {
            Personne personne = personneRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().orElseThrow());
            production.setValiderPar(personne.getPrenom() + " " + personne.getNom());
            production.setDateValider(ZonedDateTime.now());
            production.setEtat(ETATPRODUCTION.PRODUCTION);
            Personne destinataire = personneRepository.getReferenceById(production.getPersonne().getId());
            User user = userRepository.getReferenceById(destinataire.getUser().getId());
            mailService.sendEmail(
                user.getEmail(),
                "Validation de la production",
                "Votre production a été validé par " +
                personne.getPrenom() +
                " " +
                personne.getNom() +
                " le " +
                production.getDateValider().toLocalDate() +
                " et est en cours..." +
                "\n \n Cordialement M2i-SA ",
                false,
                false
            );
        }

        production = productionRepository.save(production);
        return productionMapper.toDto(production);
    }

    /**
     * Partially update a production.
     *
     * @param productionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProductionDTO> partialUpdate(ProductionDTO productionDTO) {
        log.debug("Request to partially update Production : {}", productionDTO);
        Personne personne = personneRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().orElseThrow());
        Optional<ProductionDTO> productionDTO1 = productionRepository
            .findById(productionDTO.getId())
            .map(existingProduction -> {
                productionMapper.partialUpdate(existingProduction, productionDTO);

                return existingProduction;
            })
            .map(productionRepository::save)
            .map(productionMapper::toDto);
        Production production = productionMapper.toEntity(productionDTO1.orElseThrow());

        Personne destinataire = personneRepository.getReferenceById(production.getProducteur().getId());
        User user = userRepository.getReferenceById(destinataire.getUser().getId());
        mailService.sendEmail(
            user.getEmail(),
            "Reception validée",
            "la reception de la production " +
            production.getLibelle() +
            " a été validé par " +
            personne.getPrenom() +
            " " +
            personne.getNom() +
            " le " +
            LocalDate.now() +
            "\n \n Cordialement ",
            false,
            false
        );

        return productionDTO1;
    }

    /**
     * Get all the productions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Productions");
        return productionRepository.findAll(pageable).map(productionMapper::toDto);
    }

    /**
     * Get one production by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductionDTO> findOne(Long id) {
        log.debug("Request to get Production : {}", id);
        return productionRepository.findById(id).map(productionMapper::toDto);
    }

    /**
     * Delete the production by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Production : {}", id);
        productionRepository.deleteById(id);
    }

    public List<ProductionDTO> findAllCourrier() {
        return productionRepository.findByEtat(ETATPRODUCTION.COURRIER).stream().map(productionMapper::toDto).toList();
    }
}
