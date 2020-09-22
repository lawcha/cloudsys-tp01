package ch.heiafr.pigroup6.passioncuisine.model.workshop;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "restricted_workshop")
@IdClass(WorkshopId.class)

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class WorkshopRestricted implements Serializable {

    // === Workshop table
    @Id
    @Column(name = "title", nullable = false)
    private String title;

    @Id
    @Column(name = "date", nullable = false)
    private LocalDate date;
}
