package com.nullpoint.musicroad.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Collaboration.
 */
@Entity
@Table(name = "collaboration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "collaboration")
public class Collaboration implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "proposed_date")
    private LocalDate proposedDate;

    @Column(name = "accepted")
    private Boolean accepted;

    @ManyToMany(mappedBy = "collaborations")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    //@JsonIgnore
    private Set<Band> bands = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Collaboration message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getProposedDate() {
        return proposedDate;
    }

    public Collaboration proposedDate(LocalDate proposedDate) {
        this.proposedDate = proposedDate;
        return this;
    }

    public void setProposedDate(LocalDate proposedDate) {
        this.proposedDate = proposedDate;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public Collaboration accepted(Boolean accepted) {
        this.accepted = accepted;
        return this;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Set<Band> getBands() {
        return bands;
    }

    public Collaboration bands(Set<Band> bands) {
        this.bands = bands;
        return this;
    }

    public Collaboration addBands(Band band) {
        this.bands.add(band);
        band.getCollaborations().add(this);
        return this;
    }

    public Collaboration removeBands(Band band) {
        this.bands.remove(band);
        band.getCollaborations().remove(this);
        return this;
    }

    public void setBands(Set<Band> bands) {
        this.bands = bands;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Collaboration collaboration = (Collaboration) o;
        if (collaboration.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), collaboration.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Collaboration{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", proposedDate='" + getProposedDate() + "'" +
            ", accepted='" + isAccepted() + "'" +
            "}";
    }
}
