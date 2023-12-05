package com.m2i.sgpc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Personne.
 */
@Entity
@Table(name = "personne")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Personne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "titre")
    private String titre;

    @Column(name = "numero_document")
    private String numeroDocument;

    @Column(name = "telephone")
    private String telephone;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "personne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personne", "producteur", "receveur" }, allowSetters = true)
    private Set<Production> productions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "personne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personne" }, allowSetters = true)
    private Set<Stockage> stockages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "personne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "emails", "personne" }, allowSetters = true)
    private Set<Colisage> colisages = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "personne")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "colisage", "personne" }, allowSetters = true)
    private Set<Email> emails = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "personnes" }, allowSetters = true)
    private Filiale filiale;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "producteur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personne", "producteur", "receveur" }, allowSetters = true)
    private Set<Production> producteurs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receveur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personne", "producteur", "receveur" }, allowSetters = true)
    private Set<Production> receveurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Personne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Personne nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Personne prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTitre() {
        return this.titre;
    }

    public Personne titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNumeroDocument() {
        return this.numeroDocument;
    }

    public Personne numeroDocument(String numeroDocument) {
        this.setNumeroDocument(numeroDocument);
        return this;
    }

    public void setNumeroDocument(String numeroDocument) {
        this.numeroDocument = numeroDocument;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Personne telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Set<Production> getProductions() {
        return this.productions;
    }

    public void setProductions(Set<Production> productions) {
        if (this.productions != null) {
            this.productions.forEach(i -> i.setPersonne(null));
        }
        if (productions != null) {
            productions.forEach(i -> i.setPersonne(this));
        }
        this.productions = productions;
    }

    public Personne productions(Set<Production> productions) {
        this.setProductions(productions);
        return this;
    }

    public Personne addProduction(Production production) {
        this.productions.add(production);
        production.setPersonne(this);
        return this;
    }

    public Personne removeProduction(Production production) {
        this.productions.remove(production);
        production.setPersonne(null);
        return this;
    }

    public Set<Stockage> getStockages() {
        return this.stockages;
    }

    public void setStockages(Set<Stockage> stockages) {
        if (this.stockages != null) {
            this.stockages.forEach(i -> i.setPersonne(null));
        }
        if (stockages != null) {
            stockages.forEach(i -> i.setPersonne(this));
        }
        this.stockages = stockages;
    }

    public Personne stockages(Set<Stockage> stockages) {
        this.setStockages(stockages);
        return this;
    }

    public Personne addStockage(Stockage stockage) {
        this.stockages.add(stockage);
        stockage.setPersonne(this);
        return this;
    }

    public Personne removeStockage(Stockage stockage) {
        this.stockages.remove(stockage);
        stockage.setPersonne(null);
        return this;
    }

    public Set<Colisage> getColisages() {
        return this.colisages;
    }

    public void setColisages(Set<Colisage> colisages) {
        if (this.colisages != null) {
            this.colisages.forEach(i -> i.setPersonne(null));
        }
        if (colisages != null) {
            colisages.forEach(i -> i.setPersonne(this));
        }
        this.colisages = colisages;
    }

    public Personne colisages(Set<Colisage> colisages) {
        this.setColisages(colisages);
        return this;
    }

    public Personne addColisage(Colisage colisage) {
        this.colisages.add(colisage);
        colisage.setPersonne(this);
        return this;
    }

    public Personne removeColisage(Colisage colisage) {
        this.colisages.remove(colisage);
        colisage.setPersonne(null);
        return this;
    }

    public Set<Email> getEmails() {
        return this.emails;
    }

    public void setEmails(Set<Email> emails) {
        if (this.emails != null) {
            this.emails.forEach(i -> i.setPersonne(null));
        }
        if (emails != null) {
            emails.forEach(i -> i.setPersonne(this));
        }
        this.emails = emails;
    }

    public Personne emails(Set<Email> emails) {
        this.setEmails(emails);
        return this;
    }

    public Personne addEmail(Email email) {
        this.emails.add(email);
        email.setPersonne(this);
        return this;
    }

    public Personne removeEmail(Email email) {
        this.emails.remove(email);
        email.setPersonne(null);
        return this;
    }

    public Filiale getFiliale() {
        return this.filiale;
    }

    public void setFiliale(Filiale filiale) {
        this.filiale = filiale;
    }

    public Personne filiale(Filiale filiale) {
        this.setFiliale(filiale);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Personne user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Production> getProducteurs() {
        return this.producteurs;
    }

    public void setProducteurs(Set<Production> productions) {
        if (this.producteurs != null) {
            this.producteurs.forEach(i -> i.setProducteur(null));
        }
        if (productions != null) {
            productions.forEach(i -> i.setProducteur(this));
        }
        this.producteurs = productions;
    }

    public Personne producteurs(Set<Production> productions) {
        this.setProducteurs(productions);
        return this;
    }

    public Personne addProducteur(Production production) {
        this.producteurs.add(production);
        production.setProducteur(this);
        return this;
    }

    public Personne removeProducteur(Production production) {
        this.producteurs.remove(production);
        production.setProducteur(null);
        return this;
    }

    public Set<Production> getReceveurs() {
        return this.receveurs;
    }

    public void setReceveurs(Set<Production> productions) {
        if (this.receveurs != null) {
            this.receveurs.forEach(i -> i.setReceveur(null));
        }
        if (productions != null) {
            productions.forEach(i -> i.setReceveur(this));
        }
        this.receveurs = productions;
    }

    public Personne receveurs(Set<Production> productions) {
        this.setReceveurs(productions);
        return this;
    }

    public Personne addReceveur(Production production) {
        this.receveurs.add(production);
        production.setReceveur(this);
        return this;
    }

    public Personne removeReceveur(Production production) {
        this.receveurs.remove(production);
        production.setReceveur(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personne)) {
            return false;
        }
        return getId() != null && getId().equals(((Personne) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personne{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", titre='" + getTitre() + "'" +
            ", numeroDocument='" + getNumeroDocument() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
