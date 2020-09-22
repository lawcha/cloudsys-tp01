package ch.heiafr.pigroup6.passioncuisine.model.workshop;

import ch.heiafr.pigroup6.passioncuisine.model.users.Client;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "tr_client_workshop")
@IdClass(WorkshopRegistrationId.class)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class WorkshopRegistration implements Serializable {

    @Id
    @Column(name = "email_client", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private String emailClient;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "email_client", referencedColumnName = "email")
    @JsonUnwrapped
    private Client registeredClient;

    @Id
    @Column(name = "title_workshop", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private String titleWorkshop;
    @Id
    @Column(name = "date_workshop", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private LocalDate dateWorkshop;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "title_workshop", referencedColumnName = "title"),
            @JoinColumn(name = "date_workshop", referencedColumnName = "date")
    })
    @JsonIgnore
    private WorkshopTerminated registeredWorkshop;

    public WorkshopRegistration() {
    }

    public WorkshopRegistration(String emailClient, String titleWorkshop, LocalDate dateWorkshop) {
        this.emailClient = emailClient;
        this.titleWorkshop = titleWorkshop;
        this.dateWorkshop = dateWorkshop;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public String getTitleWorkshop() {
        return titleWorkshop;
    }

    public LocalDate getDateWorkshop() {
        return dateWorkshop;
    }

    public Client getRegisteredClient() {
        return registeredClient;
    }

    public WorkshopRegistrationId getId() {
        return new WorkshopRegistrationId(emailClient, titleWorkshop, dateWorkshop);
    }
}
