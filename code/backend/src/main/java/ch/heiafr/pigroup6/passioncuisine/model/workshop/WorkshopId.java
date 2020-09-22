package ch.heiafr.pigroup6.passioncuisine.model.workshop;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WorkshopId implements Serializable {
    private String title;
    private LocalDate date;

    public WorkshopId() {
    }

    public WorkshopId(String title, LocalDate date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkshopId that = (WorkshopId) o;
        return title.equals(that.title) &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, date);
    }
}
