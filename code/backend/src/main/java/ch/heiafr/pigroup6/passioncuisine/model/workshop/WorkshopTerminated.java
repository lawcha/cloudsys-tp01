package ch.heiafr.pigroup6.passioncuisine.model.workshop;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Date;
import java.sql.SQLXML;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "workshop")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class WorkshopTerminated extends Workshop {

    @Column(name = "datetime_publication")
    protected LocalDateTime publicationDate;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    protected WorkshopState state;

    @OneToMany(mappedBy = "registeredWorkshop", fetch = FetchType.LAZY)
    @JsonIgnore
    protected List<WorkshopRegistration> registrations;

    // === Getters
    public WorkshopState getState() {
        return state;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public List<WorkshopRegistration> getRegistrations() {
        return registrations;
    }

    // === Setters
    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setState(WorkshopState state) {
        this.state = state;
    }

    public void copyFrom(WorkshopTerminated workshop) {
        this.title = workshop.title;
        this.date = workshop.date;
        this.image = workshop.image;
        this.description = workshop.description;
        this.ingredients = workshop.ingredients;
        this.maxParticipants = workshop.maxParticipants;
        this.minParticipants = workshop.minParticipants;
        this.inscriptionLimit = workshop.inscriptionLimit;
        this.street = workshop.street;
        this.location = workshop.location;
        this.emailOrganizer = workshop.emailOrganizer;
        this.category = workshop.category;
        this.state = workshop.state;
        this.publicationDate = workshop.publicationDate;
    }
}
