package ch.heiafr.pigroup6.passioncuisine.model.workshop;

import ch.heiafr.pigroup6.passioncuisine.model.location.Location;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@IdClass(WorkshopId.class)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "state",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WorkshopDraft.class, name = "draft"),
        @JsonSubTypes.Type(value = WorkshopTerminated.class, name = "published"),
        @JsonSubTypes.Type(value = WorkshopTerminated.class, name = "closed")
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Workshop implements Serializable {

    // === Workshop table
    @Id
    @Column(name = "title", nullable = false)
    protected String title;

    @Id
    @Column(name = "date", nullable = false)
    protected LocalDate date;

    @JsonInclude
    @Transient
    protected WorkshopState state;

    @Column(name = "image")
    @JsonIgnore
    protected byte[] image;

    @JsonGetter("image")
    public String getImage() {
        if (image != null)
            return new String(image);
        return null;
    }

    @JsonSetter("image")
    private void setImage(String str) {
        image = str.getBytes();
    }

    @ColumnTransformer(read = "XMLSERIALIZE (DOCUMENT description_xhtml AS character varying)", write = "XMLPARSE (DOCUMENT ?)")
    @Column(name = "description_xhtml", nullable = false, columnDefinition = "XMLType")
    protected String description;
    @ColumnTransformer(read = "XMLSERIALIZE (DOCUMENT ingredients_xml AS character varying)", write = "XMLPARSE (DOCUMENT ?)")
    @Column(name = "ingredients_xml", nullable = false, columnDefinition = "XMLType")
    protected String ingredients;

    @Column(name = "max_participants", nullable = false)
    protected short maxParticipants;
    @Column(name = "min_participants", nullable = false)
    protected short minParticipants;
    @Column(name = "datetime_inscription", nullable = false)
    protected LocalDateTime inscriptionLimit;

    @Column(name = "street", nullable = false)
    protected String street;

    // === Foreign keys and relations

    // Location
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "city", referencedColumnName = "city"),
            @JoinColumn(name = "npa", referencedColumnName = "npa")
    })
    protected Location location;

    // Organizer
    // TODO: create Organizer relation
    @Column(name = "email_organizer", nullable = false)
    protected String emailOrganizer;


    // Categories
    // TODO: create categories relation
    @Column(name = "title_category", nullable = false)
    protected String category;


    // === Getters
    public WorkshopId getId() {
        return new WorkshopId(title, date);
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public short getMaxParticipants() {
        return maxParticipants;
    }

    public short getMinParticipants() {
        return minParticipants;
    }

    public LocalDateTime getInscriptionLimit() {
        return inscriptionLimit;
    }

    public String getStreet() {
        return street;
    }

    public Location getLocation() {
        return location;
    }

    public String getEmailOrganizer() {
        return emailOrganizer;
    }

    public String getCategory() {
        return category;
    }

    // === Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
