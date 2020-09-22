package ch.heiafr.pigroup6.passioncuisine.model;

import ch.heiafr.pigroup6.passioncuisine.model.location.Location;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "organizer")

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class Organizer implements Serializable {
    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "street", nullable = false)
    private String street;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "city", referencedColumnName = "city"),
            @JoinColumn(name = "npa", referencedColumnName = "npa")
    })
    private Location location;
}
