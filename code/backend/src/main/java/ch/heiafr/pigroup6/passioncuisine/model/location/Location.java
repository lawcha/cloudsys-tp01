package ch.heiafr.pigroup6.passioncuisine.model.location;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "location")
@IdClass(LocationId.class)

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Location implements Serializable {

    @Id
    @Column(name = "npa", nullable = false)
    private short npa;

    @Id
    @Column(name = "city", nullable = false)
    private String city;

    public short getNpa() {
        return npa;
    }

    public String getCity() {
        return city;
    }
}
