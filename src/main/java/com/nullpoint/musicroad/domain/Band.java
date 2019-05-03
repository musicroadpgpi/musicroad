package com.nullpoint.musicroad.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.nullpoint.musicroad.domain.enumeration.Genre;

/**
 * A Band.
 */
@Entity
@Table(name = "band")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "band")
public class Band implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "band_name")
    private String bandName;

    @Column(name = "bio")
    private String bio;

    @Lob
    @Column(name = "cover_picture")
    private byte[] coverPicture;

    @Column(name = "cover_picture_content_type")
    private String coverPictureContentType;

    @Column(name = "component_number")
    private Integer componentNumber;

    @Column(name = "creation_year")
    private Integer creationYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("bands")
    private City city;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "band_collaborations",
               joinColumns = @JoinColumn(name = "band_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "collaborations_id", referencedColumnName = "id"))
    @JsonIgnore 
    private Set<Collaboration> collaborations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBandName() {
        return bandName;
    }

    public Band bandName(String bandName) {
        this.bandName = bandName;
        return this;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getBio() {
        return bio;
    }

    public Band bio(String bio) {
        this.bio = bio;
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public byte[] getCoverPicture() {
        return coverPicture;
    }

    public Band coverPicture(byte[] coverPicture) {
        this.coverPicture = coverPicture;
        return this;
    }

    public void setCoverPicture(byte[] coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getCoverPictureContentType() {
        return coverPictureContentType;
    }

    public Band coverPictureContentType(String coverPictureContentType) {
        this.coverPictureContentType = coverPictureContentType;
        return this;
    }

    public void setCoverPictureContentType(String coverPictureContentType) {
        this.coverPictureContentType = coverPictureContentType;
    }

    public Integer getComponentNumber() {
        return componentNumber;
    }

    public Band componentNumber(Integer componentNumber) {
        this.componentNumber = componentNumber;
        return this;
    }

    public void setComponentNumber(Integer componentNumber) {
        this.componentNumber = componentNumber;
    }

    public Integer getCreationYear() {
        return creationYear;
    }

    public Band creationYear(Integer creationYear) {
        this.creationYear = creationYear;
        return this;
    }

    public void setCreationYear(Integer creationYear) {
        this.creationYear = creationYear;
    }

    public Genre getGenre() {
        return genre;
    }

    public Band genre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public User getUser() {
        return user;
    }

    public Band user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public City getCity() {
        return city;
    }

    public Band city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<Collaboration> getCollaborations() {
        return collaborations;
    }

    public Band collaborations(Set<Collaboration> collaborations) {
        this.collaborations = collaborations;
        return this;
    }

    public Band addCollaborations(Collaboration collaboration) {
        this.collaborations.add(collaboration);
        collaboration.getBands().add(this);
        return this;
    }

    public Band removeCollaborations(Collaboration collaboration) {
        this.collaborations.remove(collaboration);
        collaboration.getBands().remove(this);
        return this;
    }

    public void setCollaborations(Set<Collaboration> collaborations) {
        this.collaborations = collaborations;
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
        Band band = (Band) o;
        if (band.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), band.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Band{" +
            "id=" + getId() +
            ", bandName='" + getBandName() + "'" +
            ", bio='" + getBio() + "'" +
            ", coverPicture='" + getCoverPicture() + "'" +
            ", coverPictureContentType='" + getCoverPictureContentType() + "'" +
            ", componentNumber=" + getComponentNumber() +
            ", creationYear=" + getCreationYear() +
            ", genre='" + getGenre() + "'" +
            "}";
    }
}
