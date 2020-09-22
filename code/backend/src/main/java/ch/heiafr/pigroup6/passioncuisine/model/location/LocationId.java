package ch.heiafr.pigroup6.passioncuisine.model.location;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LocationId implements Serializable {

    private short npa;

    private String city;

    public LocationId() {
    }

    public LocationId(short npa, String city) {
        this.npa = npa;
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationId that = (LocationId) o;
        return npa == that.npa &&
                city.equals(that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(npa, city);
    }
}
