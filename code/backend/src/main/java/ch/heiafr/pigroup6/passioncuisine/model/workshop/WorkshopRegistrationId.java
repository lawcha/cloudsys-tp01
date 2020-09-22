package ch.heiafr.pigroup6.passioncuisine.model.workshop;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class WorkshopRegistrationId implements Serializable {
    private String emailClient;
    private String titleWorkshop;
    private LocalDate dateWorkshop;

    public WorkshopRegistrationId() {
    }

    public WorkshopRegistrationId(String emailClient, String titleWorkshop, LocalDate dateWorkshop) {
        this.emailClient = emailClient;
        this.titleWorkshop = titleWorkshop;
        this.dateWorkshop = dateWorkshop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkshopRegistrationId that = (WorkshopRegistrationId) o;
        return emailClient.equals(that.emailClient) &&
                titleWorkshop.equals(that.titleWorkshop) &&
                dateWorkshop.equals(that.dateWorkshop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailClient, titleWorkshop, dateWorkshop);
    }
}
