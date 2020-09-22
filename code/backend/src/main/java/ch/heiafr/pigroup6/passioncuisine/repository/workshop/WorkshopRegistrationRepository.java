package ch.heiafr.pigroup6.passioncuisine.repository.workshop;

import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopRegistration;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface WorkshopRegistrationRepository extends JpaRepository<WorkshopRegistration, WorkshopRegistrationId> {
    List<WorkshopRegistration> findAllByDateWorkshopEqualsAndTitleWorkshopEquals(LocalDate dateWorkshop, String titleWorkshop);

    @Transactional
    @Modifying
    @Query(value = "insert into tr_client_workshop (email_client, date_workshop, title_workshop) values (?1, ?2, ?3)", nativeQuery = true)
    void save(String email, LocalDate date, String title);
}
